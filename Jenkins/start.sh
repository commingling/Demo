#!/bin/bash

#######  变量  #########
APP_NAME=$1
LOG_DIR="/home/www/$APP_NAME" # 假设日志存放路径

# 确保日志目录存在
mkdir -p $LOG_DIR

# JVM参数
JVM_OPTS="-Dname=$APP_NAME -Duser.timezone=Asia/Shanghai -Xms1024m -Xmx1024m -XX:-OmitStackTraceInFastThrow -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=35"

###### Stopping  #########
echo "Stopping existing instance of $APP_NAME"
pid=$(ps -ef | grep $APP_NAME.jar | grep -v grep | awk '{print $2}')
sudo chown -R www:www /home/www/$APP_NAME
if [ -n "$pid" ]; then
    sudo kill -15 $pid  # Send SIGTERM
    echo "Waiting for $APP_NAME to stop..."

    # 等待进程停止，最多等待一定的时间（例如 30 秒）
    timeout=30
    while [ $timeout -gt 0 ]; do
        if ! ps -p $pid > /dev/null; then
            echo "$APP_NAME stopped successfully."
            break
        fi

        sleep 1
        ((timeout--))
    done

    if [ $timeout -le 0 ]; then
        echo "Force stopping $APP_NAME (PID: $pid)"
        sudo kill -9 $pid
    fi

    # 等待额外的时间确保系统释放内存
    echo "Waiting additional time for memory release..."
    sleep 30
fi

####### Start  #########
echo "Starting $APP_NAME"
cd $APP_NAME || { echo "Failed to change directory to $APP_NAME. Exiting."; exit 1; }

/apps/product/jdk1.8.0_351/bin/java $JVM_OPTS -jar $APP_NAME.jar >> /dev/null 2>&1 &

java_pid=$!

# Check if the Java process has started
if ps -p $java_pid > /dev/null; then
    echo "Start successful, PID: $java_pid"
else
    echo "Start failed, see $LOG_FILE for details"
    exit 1  # Exit with an error code
fi
