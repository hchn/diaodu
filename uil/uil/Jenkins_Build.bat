::���ñ������Ļ�������
SET JAVA_HOME=D:\jdk1.6.0_24
::���ð汾��
SET VERSION=%VERSION%
::���ñ���·��
SET BUILD_DIR=D:\Jenkins_Workspace\workspace\T30V1.0_Buid_Common_App
::���ð汾����·��
SET RELEASE_DIR=\\192.168.2.77\�з��Բ�汾������\T30\T30_V1.0\common_app

ant -f Jenkins_Build.xml
