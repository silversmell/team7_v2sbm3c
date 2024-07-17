package dev.mvc.tip_contents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Component("dev.mvc.tip_contents.TipContentsProc")
public class TipContentsProc implements TipContentsProcInter {
	@Autowired
	Security security;
	
	@Autowired
	private TipContentsDAOInter tcontentsDAO;

//	@Override
//	public int create(TipContentsVO tcontentsVO) {
//		String passwd = tcontentsVO.getTcon_passwd();
//		String passwd_encoded = this.security.aesEncode(passwd);
//		tcontentsVO.setTcon_passwd(passwd_encoded);
//		
//		int cnt = this.tcontentsDAO.create(tcontentsVO);
//		return cnt;
//	}
	
	@Override
	public int create(TipContentsVO tcontentsVO) {
	    String passwd = tcontentsVO.getTcon_passwd();
	    String passwd_encoded = this.security.aesEncode(passwd);
	    tcontentsVO.setTcon_passwd(passwd_encoded);
	    

        StringBuilder tcon_img_sb = new StringBuilder();
        StringBuilder tcon_saved_img_sb = new StringBuilder();
        StringBuilder tcon_thumb_img_sb = new StringBuilder();
        long total_img_size = 0;

        String upDir = TipContents.getUploadDir();

        for (MultipartFile mf : tcontentsVO.getImg_mf()) {
            if (mf != null && !mf.isEmpty()) {
                String img = mf.getOriginalFilename();
                long img_size = mf.getSize();

                if (img_size > 0 && Tool.isImage(img)) {
                    String saved_img = Upload.saveFileSpring(mf, upDir);
                    String thumb_img = Tool.preview(upDir, saved_img, 200, 150);

                    if (tcon_img_sb.length() > 0) tcon_img_sb.append(";");
                    if (tcon_saved_img_sb.length() > 0) tcon_saved_img_sb.append(";");
                    if (tcon_thumb_img_sb.length() > 0) tcon_thumb_img_sb.append(";");

                    tcon_img_sb.append(img);
                    tcon_saved_img_sb.append(saved_img);
                    tcon_thumb_img_sb.append(thumb_img);

                    total_img_size += img_size;
                }
            }
        }

        tcontentsVO.setTcon_img(tcon_img_sb.toString());
        tcontentsVO.setTcon_saved_img(tcon_saved_img_sb.toString());
        tcontentsVO.setTcon_thumb_img(tcon_thumb_img_sb.toString());
        tcontentsVO.setTcon_img_size(total_img_size);


	    
	    int cnt = this.tcontentsDAO.create(tcontentsVO);
	    return cnt;
	}
	
	@Override
	public int youtube(HashMap<String, Object> map) {
		int cnt = this.tcontentsDAO.youtube(map);
		return cnt;
	}

	@Override
	public int list_count(HashMap<String, Object> map) {
		int cnt = this.tcontentsDAO.list_count(map);
		return cnt;
	}

	@Override
	public ArrayList<TipContentsVO> list(HashMap<String, Object> map) {
		ArrayList<TipContentsVO> list = this.tcontentsDAO.list(map);
		return list;
	}
	
	@Override
	public ArrayList<TipContentsVO> tconImages(int tcon_no) {
		ArrayList<TipContentsVO> images = this.tcontentsDAO.tconImages(tcon_no);
		return images;
	}

	@Override
	public TipContentsVO read(int tcon_no) {
		TipContentsVO tcontentsVO = this.tcontentsDAO.read(tcon_no);
		
        List<String> imgFiles = new ArrayList<>();
        List<String> savedImgFiles = new ArrayList<>();
        List<String> thumbImgFiles = new ArrayList<>();

        // Convert stored image paths into List using StringTokenizer
        if (tcontentsVO.getTcon_img() != null && !tcontentsVO.getTcon_img().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(tcontentsVO.getTcon_img(), ";");
            while (tokenizer.hasMoreTokens()) {
                imgFiles.add(tokenizer.nextToken());
            }
        }
        if (tcontentsVO.getTcon_saved_img() != null && !tcontentsVO.getTcon_saved_img().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(tcontentsVO.getTcon_saved_img(), ";");
            while (tokenizer.hasMoreTokens()) {
                savedImgFiles.add(tokenizer.nextToken());
            }
        }
        if (tcontentsVO.getTcon_thumb_img() != null && !tcontentsVO.getTcon_thumb_img().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(tcontentsVO.getTcon_thumb_img(), ";");
            while (tokenizer.hasMoreTokens()) {
                thumbImgFiles.add(tokenizer.nextToken());
            }
        }

        // Set lists in VO
        tcontentsVO.setTcon_img(String.join(";", imgFiles));
        tcontentsVO.setTcon_saved_img(String.join(";", savedImgFiles));
        tcontentsVO.setTcon_thumb_img(String.join(";", thumbImgFiles));
		
		return tcontentsVO;
	}
	
	@Override
	public int updateViews(int tcon_no) {
		int cnt = this.tcontentsDAO.updateViews(tcon_no);
		return cnt;
	}
	
	public int update(TipContentsVO tcontentsVO) {
		int cnt = this.tcontentsDAO.update(tcontentsVO);
		return cnt;
	}
	
	@Override
	public int delete(int tcon_no) {
		int cnt = this.tcontentsDAO.delete(tcon_no);
		return cnt;
	}

	@Override
	public boolean isLiked(Map<String, Object> map) {
	    boolean check = this.tcontentsDAO.isLiked(map);
		return check;
	}
	
	@Override
	public int insertLike(Map<String, Object> map) {
		int cnt = this.tcontentsDAO.insertLike(map);
		return cnt;
	}

	@Override
	public int deleteLike(Map<String, Object> map) {
		int cnt = this.tcontentsDAO.deleteLike(map);
		return cnt;
	}

	@Override
	public int like_count(int tcon_no) {
		int cnt = this.tcontentsDAO.like_count(tcon_no);
		return cnt;
	}

	
}
