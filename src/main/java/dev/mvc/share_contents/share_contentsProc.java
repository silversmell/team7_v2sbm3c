package dev.mvc.share_contents;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.share_contents.share_contentsProc")
public class share_contentsProc implements share_contentsProcInter {
	@Autowired
	private share_contentsDAOInter share_contentsDAO;

	@Override
	public ArrayList list_all() {
		
		return null;
	}

	@Override
	public ArrayList list_all_image() {
		// TODO Auto-generated method stub
		return null;
	}

}
