@echo off
setlocal enabledelayedexpansion
:: �����ǰĿ¼
echo ��ǰĿ¼��: %cd%

:: ���Node.js�Ƿ�װ
node -v >nul 2>&1
if not !errorlevel! == 0 (
    echo Node.jsδ��װ�����Ȱ�װNode.js�������д˽ű���
    exit
)


:: ���node_modules�ļ����Ƿ����
if not exist node_modules (
    echo node_modules�ļ��в����ڣ����ڰ�װ��Ŀ���������Ժ�...
    call npm install
    if !errorlevel! neq 0 (
        echo ��Ŀ������װ�����г���
        exit
    )
    echo ��Ŀ������װ��ɡ�
) else (
    yarn
    echo node_modules�ļ����Ѵ��ڣ�����������װ��
)

pause