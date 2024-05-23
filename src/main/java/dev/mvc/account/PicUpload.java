package dev.mvc.account;

import java.io.File;

public class PicUpload {

    // Windows, VMWare, AWS cloud 절대 경로 설정
    public static synchronized String getUploadDir() {
        String path = "";
        String osName = System.getProperty("os.name").toLowerCase();

        if (File.separator.equals("\\")) { // windows, 개발 환경의 파일 업로드 폴더
            // path = "C:/kd/deploy/resort_v2sbm3c/contents/storage/";
            path="C:\\kd\\deploy\\team7\\contents\\storage\\";
            // System.out.println("Windows 10: " + path);
        } else if (osName.contains("mac")) { // MacOS 로컬 경로
            path = "/Users/youngeun/deploy/team7/contents/storage/";
            // System.out.println("MacOS: " + path);
        } else { // Linux, AWS, 서비스용 배치 폴더 
            // System.out.println("Linux");
            path = "/home/ubuntu/deploy/team7/contents/storage/";
        }
        
        return path;
    }
    
}

