from datetime import datetime, timedelta
from elasticsearch import Elasticsearch
import time

# 初始化 Elasticsearch 客户端
es = Elasticsearch(
    ['https://es-hdcxiyco.public.tencentelasticsearch.com:9200'],
    http_auth=('elastic', 'pikaqiu112.!'),
    timeout=60  # 请求超时时间设置为 60 秒
)

# 设置起始和结束日期
start_date = datetime(2023, 12, 1)
end_date = datetime.now()

# 当前日期，用于循环中
current_date = start_date

# 分批次按周重建索引
time_delta = timedelta(weeks=1)

def check_task_status(task_id):
    time.sleep(10)  # 等待10秒后开始检查任务状态
    while True:
        try:
            task_response = es.tasks.get(task_id=task_id)
            if task_response['completed']:
                print(f"Task {task_id} is completed.")
                break
            else:
                print(f"Task {task_id} is still running...")
                time.sleep(30)  # 每60秒检查一次任务状态
        except Exception as e:
            print(f"Error checking task status: {e}")
            break

while current_date < end_date:
    # 设置下一个间隔的日期
    next_interval = current_date + time_delta
    if next_interval > end_date:
        next_interval = end_date

    # 构建 reindex 请求体
    body = {
        "source": {
            "index": "flow_trace_log",
            "query": {
                "range": {
                    "@timestamp": {
                        "gte": current_date.strftime("%Y-%m-%d"),
                        "lt": next_interval.strftime("%Y-%m-%d")
                    }
                }
            }
        },
        "dest": {
            "index": "flow_trace_log_new1"
        }
    }

    try:
        # 发起异步 reindex 请求
        response = es.reindex(
            body=body,
            wait_for_completion=False,  # 异步执行
            request_timeout=30 * 10,  # 请求超时时间
            slices='auto',  # 启用自动分片
            requests_per_second=1000  # 降低请求速率以减轻集群负载
        )
        task_id = response['task']
        print(f"Started reindex task {task_id} for data from {current_date.strftime('%Y-%m-%d')} to {next_interval.strftime('%Y-%m-%d')}")

        # 检查异步任务状态
        check_task_status(task_id)
    except Exception as e:
        print(f"An error occurred while reindexing: {e}")
        break  # 出现错误时中断循环

    current_date = next_interval
