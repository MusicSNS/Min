package com.co.kr.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.BoardContentDomain;
import com.co.kr.domain.BoardFileDomain;
import com.co.kr.domain.BoardListDomain;

@Mapper
public interface BoardMapper {

	//list
	public List<BoardListDomain> boardList();
	
	//content upload
	public void bdcontentUpload(BoardContentDomain boardContentDomain);
	//file upload
	public void bdfileUpload(BoardFileDomain boardFileDomain);

	//content update
	public void bdContentUpdate(BoardContentDomain boardContentDomain);
	//file update
	public void bdFileUpdate(BoardFileDomain boardFileDomain);

  //content delete 
	public void bdContentRemove(HashMap<String, Object> map);
	//file delete 
	public void bdFileRemove(BoardFileDomain boardFileDomain);
	//select one
	public BoardListDomain boardSelectOne(HashMap<String, Object> map);

	//select one file
	public List<BoardFileDomain> boardSelectOneFile(HashMap<String, Object> map);
	
	//검색창
	public List<BoardListDomain> boardSelectSelect(HashMap<String, Object> map);
	
	//전체 개수
	public int bdGetAll();
	
	public List<BoardListDomain> boardAllList(HashMap<String, Object> map);
}
