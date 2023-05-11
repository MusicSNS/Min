package com.co.kr.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.BoardFileDomain;
import com.co.kr.domain.BoardListDomain;
import com.co.kr.vo.FileListVO;

public interface BoardService {

	// 인서트
	public int fileProcess(FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq);
	
	// 전체 리스트 조회   // 지난시간 작성
	public List<BoardListDomain> boardList();

	// 하나 삭제
	public void bdContentRemove(HashMap<String, Object> map);

	// 하나 삭제
	public void bdFileRemove(BoardFileDomain boardFileDomain);
	// 하나 리스트 조회
		public BoardListDomain boardSelectOne(HashMap<String, Object> map);
		// 하나 파일 리스트 조회
		public List<BoardFileDomain> boardSelectOneFile(HashMap<String, Object> map);
		
		//검색창
		public List<BoardListDomain> boardSelectSelect(HashMap<String, Object> map);
		
		//전체 개수
		public int bdGetAll();
		
		public List<BoardListDomain> boardAllList(HashMap<String, Object> map);
}
