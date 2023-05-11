package com.co.kr.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.BoardCommentContentDomain;
import com.co.kr.domain.BoardCommentListDomain;

@Mapper
public interface BoardCommentMapper {

	//list
	public List<BoardCommentListDomain> boardcommentList(HashMap<String, Object> map);
	
	//select one
	public BoardCommentListDomain boardcommentSelectOne(HashMap<String, Object> map);
	
	//content upload
	public void boardcommentContentUpload(BoardCommentContentDomain boardCommentContentDomain);

	//content update
	public void boardcommentContentUpdate(BoardCommentContentDomain boardCommentContentDomain);

	//content delete 
	public void boardcommentContentRemove(HashMap<String, Object> map);
	
	//every content delete
	public void boardcommentAllContentRemove(HashMap<String, Object> map);
	
}