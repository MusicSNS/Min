package com.co.kr.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.BoardCommentListDomain;
import com.co.kr.vo.FileListVO;

public interface BoardCommentService {
	
	// 전체 리스트 조회   // 지난시간 작성
	public List<BoardCommentListDomain> boardcommentList(HashMap<String, Object> map);
	
	// 하나 리스트 조회
	public BoardCommentListDomain boardcommentSelectOne(HashMap<String, Object> map);
	
	// 인서트
	public int fileProcess(FileListVO fileListVO, HttpServletRequest httpReq);

	// 하나 삭제
	public void boardcommentContentRemove(HashMap<String, Object> map);
	
	// 모두 삭제
	public void boardcommentAllContentRemove(HashMap<String, Object> map);
}
