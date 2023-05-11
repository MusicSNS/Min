package com.co.kr.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.code.Code;
import com.co.kr.domain.BoardCommentContentDomain;
import com.co.kr.domain.BoardCommentListDomain;
import com.co.kr.domain.BoardContentDomain;
import com.co.kr.exception.RequestException;
import com.co.kr.mapper.BoardCommentMapper;
import com.co.kr.util.CommonUtils;
import com.co.kr.vo.FileListVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BoardCommentServiceImpl implements BoardCommentService {

	@Autowired
	private BoardCommentMapper boardCommentMapper;
	
	@Override
	public List<BoardCommentListDomain> boardcommentList(HashMap<String, Object> map) {
		return boardCommentMapper.boardcommentList(map);
	}
	@Override
	public int fileProcess(FileListVO fileListVO, HttpServletRequest httpReq) {
		//session 생성
		HttpSession session = httpReq.getSession();
		
		//content domain 생성 
		BoardCommentContentDomain boardCommentContentDomain = BoardCommentContentDomain.builder()
				.bdSeq(fileListVO.getBdbcseq())
				.mbId(session.getAttribute("id").toString())
				.mbName(session.getAttribute("mbname").toString())
				.bcContent(fileListVO.getContent())
				.build();
		
				if(fileListVO.getIsEdit() != null) {
					boardCommentContentDomain.setBcSeq(Integer.parseInt(fileListVO.getSeq()));
					System.out.println("수정업데이트");
					// db 업데이트
					boardCommentMapper.boardcommentContentUpdate(boardCommentContentDomain);
				}else {	
					// db 인서트
					boardCommentMapper.boardcommentContentUpload(boardCommentContentDomain);
					System.out.println(" db 인서트");
				}
				
				int bdSeq = Integer.parseInt(boardCommentContentDomain.getBdSeq());

				return bdSeq; // 저장된 게시판 번호
	}

	@Override
	public void boardcommentContentRemove(HashMap<String, Object> map) {
		boardCommentMapper.boardcommentContentRemove(map);
	}

	// 하나만 가져오기
	@Override
	public BoardCommentListDomain boardcommentSelectOne(HashMap<String, Object> map) {
		return boardCommentMapper.boardcommentSelectOne(map);
	}
	
	// 모두 삭제
	public void boardcommentAllContentRemove(HashMap<String, Object> map) {
		boardCommentMapper.boardcommentAllContentRemove(map);
	};
	
}