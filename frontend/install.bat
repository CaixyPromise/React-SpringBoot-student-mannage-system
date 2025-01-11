@echo off
setlocal enabledelayedexpansion
:: 输出当前目录
echo 当前目录是: %cd%

:: 检查Node.js是否安装
node -v >nul 2>&1
if not !errorlevel! == 0 (
    echo Node.js未安装，请先安装Node.js后再运行此脚本。
    exit
)


:: 检查node_modules文件夹是否存在
if not exist node_modules (
    echo node_modules文件夹不存在，正在安装项目依赖，请稍候...
    call npm install
    if !errorlevel! neq 0 (
        echo 项目依赖安装过程中出错。
        exit
    )
    echo 项目依赖安装完成。
) else (
    yarn
    echo node_modules文件夹已存在，跳过依赖安装。
)

pause