package dev.mvc.account;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 테이블 설계 
 *
CREATE TABLE ACCOUNT(
		ACC_NO NUMBER(10) NOT NULL PRIMARY KEY,
		ACC_ID VARCHAR2(60) NOT NULL,
		ACC_PW VARCHAR2(100) NOT NULL,
		ACC_NAME VARCHAR2(20) NOT NULL,
		ACC_TEL VARCHAR2(14),
		ACC_AGE VARCHAR2(6),
		ACC_DATE DATE NOT NULL,
		ACC_GRADE NUMBER(2) NOT NULL,
		ACC_IMG VARCHAR2(100),
		ACC_SAVED_IMG VARCHAR2(100),
		ACC_THUMB_IMG VARCHAR2(100),
		ACC_IMG_SIZE NUMBER(10)
);
*/

@Setter @Getter
public class AccountVO {
	/** 회원 번호 */
	private int acc_no;
	/** 아이디(이메일) */
	private String acc_id = "";
	/** 패스워드 */
	private String acc_pw = "";
	/** 이름 */
	private String acc_name = "";
	/** 전화번호 */
	private String acc_tel = "";
	/** 연령대 */
	private String acc_age = "";
	/** 가입일 */
	private String acc_date = "";
	/** 회원 등급 */
	private int acc_grade = 0;
	
	/** 업로드 이미지 파일 */
	private MultipartFile acc_img_mf = null;
	/** 메인 이미지 크기 단위, 파일 크기 */
	private String img_size_label = "";
	/** 프로필 사진 */
	private String acc_img = "";
	/** 저장된 프로필 사진 */
	private String acc_saved_img = "";
	/** 프로필 사진 THUMB */
	private String acc_thumb_img = "";
	/** 프로필 사진 크기 */
	private long acc_img_size = 0;
	
}
