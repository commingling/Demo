#!/bin/bash

# 替换以下变量的值
gitlab_domain="http://120.53.91.142/"  # 你的 GitLab 实例域名
api_token="glpat-bbQt6PaDChvDCRMitdCi"  # 你的 GitLab API 令牌
target_directory="/Users/lvmeijuan/SkyTours"  # 本地存储项目的目录

# 创建目标目录
mkdir -p $target_directory
cd $target_directory

# 调用 GitLab API 获取项目列表
curl --header "PRIVATE-TOKEN: $api_token" "$gitlab_domain/api/v4/projects?membership=true&per_page=100" | jq -r '.[] | .ssh_url_to_repo' | while read repo
do
    git clone $repo
done
