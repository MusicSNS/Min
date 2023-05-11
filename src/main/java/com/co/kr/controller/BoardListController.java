package com.co.kr.controller;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.code.Code;
import com.co.kr.domain.LoginDomain;
import com.co.kr.domain.BoardCommentContentDomain;
import com.co.kr.domain.BoardCommentListDomain;
import com.co.kr.domain.BoardFileDomain;
import com.co.kr.domain.BoardListDomain;
import com.co.kr.exception.RequestException;
import com.co.kr.service.BoardCommentService;
import com.co.kr.service.BoardService;
import com.co.kr.util.Pagination;
import com.co.kr.vo.FileListVO;
import com.co.kr.vo.LoginVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardListController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardCommentService boardCommentService;
	
	private String MenuName = "Board";
		
	@RequestMapping(value = "board")
	public ModelAndView board(LoginVO loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//session 처리
		ModelAndView mav = new ModelAndView();
		Map<String, String> map = new HashMap();

		List<BoardListDomain> items = boardService.boardList();
		mav.addObject("items", items);
		
		mav.addObject("menuname", MenuName);
		mav.setViewName("board/boardList.html"); 
		
		return mav;
	};

	@PostMapping(value = "/bddetail")
	//리스트 하나 가져오기 따로 함수뺌
		public ModelAndView bdSelectOneCall(@ModelAttribute("fileListVO") FileListVO fileListVO, String bdSeq, HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();
			HashMap<String, Object> map = new HashMap<String, Object>();
			HttpSession session = request.getSession();
			
			map.put("bdSeq", Integer.parseInt(bdSeq));
			BoardListDomain boardListDomain = boardService.boardSelectOne(map);
			System.out.println("boardListDomain"+boardListDomain);
			List<BoardFileDomain> fileList =  boardService.boardSelectOneFile(map);
			List<BoardCommentListDomain> boardCommentListDomains = boardCommentService.boardcommentList(map);
			System.out.println(boardCommentListDomains);
			for (BoardFileDomain list : fileList) {
				String path = list.getUpFilePath().replaceAll("\\\\", "/");
				list.setUpFilePath(path);
			}
			
			mav.addObject("menuname", MenuName);
			mav.addObject("bcitems", boardCommentListDomains);
			mav.addObject("bddetail", boardListDomain);
			mav.addObject("files", fileList);
			mav.setViewName("board/boardList.html");
			//삭제시 사용할 용도
			session.setAttribute("files", fileList);

			return mav;
		}
	
/*	public ModelAndView scSelect(FileListVO fileListVO) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("stSeq", fileListVO.getStscseq());
		List<StudyCommentListDomain> boardCommentListDomains = boardCommentService.studycommentList(map);
		mav.addObject("scitems", boardCommentListDomains);
		return mav;
	} */
	
	
	@PostMapping(value = "bdupload")
	public ModelAndView bdUpload(FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws IOException, ParseException {
		
		ModelAndView mav = new ModelAndView();
		int bdSeq = boardService.fileProcess(fileListVO, request, httpReq);
		fileListVO.setContent(""); //초기화
		fileListVO.setTitle(""); //초기화
		
		// 화면에서 넘어올때는 bdSeq String이라 string으로 변환해서 넣어즘
		mav = bdSelectOneCall(fileListVO, String.valueOf(bdSeq),request);
		mav.addObject("menuname", MenuName);
		mav.setViewName("board/boardList.html");
		return mav;
		
	}
	
	@PostMapping(value = "bcupload")
	public ModelAndView bcUpload(FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws IOException, ParseException {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<>();
		
		int bdSeq = boardCommentService.fileProcess(fileListVO, httpReq);
		fileListVO.setContent(""); //초기화
		map.put("bdSeq", bdSeq);
		List<BoardCommentListDomain> boardCommentListDomains = boardCommentService.boardcommentList(map);
		
		mav = bdSelectOneCall(fileListVO, String.valueOf(bdSeq), request);
		mav.addObject("bcitems", boardCommentListDomains);
		mav.setViewName("board/boardList.html");
		return mav;
		
	}
	
	  // 좌측 메뉴 클릭시 보드화면 이동 (로그인된 상태)
		@RequestMapping(value = "bdList")
		public ModelAndView bdList() { 
			ModelAndView mav = new ModelAndView();
			List<BoardListDomain> items = boardService.boardList();
			mav.addObject("items", items);
			mav.setViewName("board/boardList.html");
			return mav; 
		};
	
	//detail
		@GetMapping("bddetail")
	    public ModelAndView bdDetail(@ModelAttribute("fileListVO") FileListVO fileListVO, @RequestParam("bdSeq") String bdSeq, HttpServletRequest request) throws IOException {
			ModelAndView mav = new ModelAndView();
			//하나파일 가져오기
			mav = bdSelectOneCall(fileListVO, bdSeq,request);
			mav.addObject("menuname", MenuName);
			mav.setViewName("board/boardList.html");
			return mav;
		}
		@GetMapping("bdedit")
			public ModelAndView bdedit(FileListVO fileListVO, @RequestParam("bdSeq") String bdSeq, HttpServletRequest request) throws IOException {
				ModelAndView mav = new ModelAndView();

				HashMap<String, Object> map = new HashMap<String, Object>();
				HttpSession session = request.getSession();
				
				map.put("bdSeq", Integer.parseInt(bdSeq));
				BoardListDomain boardListDomain =boardService.boardSelectOne(map);
				List<BoardFileDomain> fileList =  boardService.boardSelectOneFile(map);
				
				for (BoardFileDomain list : fileList) {
					String path = list.getUpFilePath().replaceAll("\\\\", "/");
					list.setUpFilePath(path);
				}

				fileListVO.setSeq(boardListDomain.getBdSeq());
				fileListVO.setContent(boardListDomain.getBdContent());
				fileListVO.setTitle(boardListDomain.getBdTitle());
				fileListVO.setIsEdit("edit");  // upload 재활용하기위해서
				
				mav.addObject("bddetail", boardListDomain);
				mav.addObject("files", fileList);
				mav.addObject("fileLen",fileList.size());
				
				mav.setViewName("board/boardEditList.html");
				return mav;
			}
		
		@GetMapping("bcedit")
		public ModelAndView bcedit(FileListVO fileListVO, @RequestParam("bcSeq") String bcSeq, HttpServletRequest request) throws IOException {
			
			ModelAndView mav = new ModelAndView();
			HashMap<String, Object> map = new HashMap<String, Object>();
			HttpSession session = request.getSession();
			
			map.put("bcSeq", bcSeq);
			BoardCommentListDomain boardCommentListDomain = boardCommentService.boardcommentSelectOne(map);

			fileListVO.setSeq(boardCommentListDomain.getBcSeq());
			fileListVO.setContent(boardCommentListDomain.getBcContent());
			fileListVO.setIsEdit("edit");  // upload 재활용하기위해서
			
			mav.addObject("bcitems", boardCommentListDomain);
			mav.setViewName("board/boardEditList.html");
			return mav;
		}
		
		@PostMapping("bdeditSave")
			public ModelAndView bdeditSave(@ModelAttribute("fileListVO") FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws IOException {
				ModelAndView mav = new ModelAndView();
				//저장
				boardService.fileProcess(fileListVO, request, httpReq);
				
				
				mav = bdSelectOneCall(fileListVO, fileListVO.getSeq(),request);
				fileListVO.setContent(""); //초기화
				fileListVO.setTitle(""); //초기화
				mav.setViewName("board/boardList.html");
				return mav;
			}
		
		@PostMapping("bceditSave")
		public ModelAndView bceditSave(@ModelAttribute("fileListVO") FileListVO fileListVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws IOException {
			ModelAndView mav = new ModelAndView();
			//저장
			int bdSeq = boardCommentService.fileProcess(fileListVO, httpReq);
			HashMap<String, Object> map = new HashMap<>();
			
			map.put("bdSeq", bdSeq);
			List<BoardCommentListDomain> boardCommentListDomains = boardCommentService.boardcommentList(map);
			
			mav = bdSelectOneCall(fileListVO, String.valueOf(bdSeq),request);
			mav.addObject("bcitems", boardCommentListDomains);
			fileListVO.setContent(""); //초기화
			fileListVO.setTitle(""); //초기화
			mav.setViewName("board/boardList.html");
			return mav;
		}
		
		@GetMapping("bdremove")
		public ModelAndView bdRemove(@RequestParam("bdSeq") String bdSeq, HttpServletRequest request) throws IOException {
			ModelAndView mav = new ModelAndView();
			
			HttpSession session = request.getSession();
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<BoardFileDomain> fileList = null;
			if(session.getAttribute("files") != null) {						
				fileList = (List<BoardFileDomain>) session.getAttribute("files");
			}
			
			map.put("bdSeq", Integer.parseInt(bdSeq));
			
			//내용삭제
			boardService.bdContentRemove(map);

			for (BoardFileDomain list : fileList) {
				list.getUpFilePath();
				Path filePath = Paths.get(list.getUpFilePath());
		 
		        try {
		        	
		            // 파일 물리삭제
		            Files.deleteIfExists(filePath); // notfound시 exception 발생안하고 false 처리
		            // db 삭제 
								boardService.bdFileRemove(list);
					
		        } catch (DirectoryNotEmptyException e) {
								throw RequestException.fire(Code.E404, "디렉토리가 존재하지 않습니다", HttpStatus.NOT_FOUND);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
			bcAllRemove(bdSeq);
			//세션해제
			session.removeAttribute("files"); // 삭제
			
			mav = Paging(request);
			return mav; 
		}

		public void bcAllRemove(String bdSeq) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("bdSeq", bdSeq);
			boardCommentService.boardcommentAllContentRemove(map);
		}
		
		@GetMapping("bcremove")
		public ModelAndView bcRemove(@RequestParam("bcSeq") String bcSeq, @RequestParam("bdSeq") String bdSeq, FileListVO fileListVO ,HttpServletRequest request) throws IOException {
			ModelAndView mav = new ModelAndView();
			
			HttpSession session = request.getSession();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("bcSeq", Integer.parseInt(bcSeq));
			
			//내용삭제
			boardCommentService.boardcommentContentRemove(map);
			
			map.put("bdSeq", Integer.parseInt(bdSeq));
			List<BoardCommentListDomain> boardCommentListDomains = boardCommentService.boardcommentList(map);
			mav = bdSelectOneCall(fileListVO, bdSeq, request);
			mav.addObject("bcitems", boardCommentListDomains);
			mav.setViewName("board/boardList.html");
			
			return mav;
		}
		
		@RequestMapping("boardselect")
		public ModelAndView boardSelect(FileListVO fileListVO) {
			ModelAndView mav = new ModelAndView();
			String keyword = fileListVO.getKeyword();
			HashMap<String, Object> map = new HashMap<>();
			map.put("bdTitle", keyword);
			List<BoardListDomain> boardListDomains = boardService.boardSelectSelect(map);
			mav.addObject("items", boardListDomains);
			mav.setViewName("board/boardList.html");
			return mav;
		}


	//리스트 가져오기 따로 함수뺌
	public ModelAndView bdListCall(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		//페이지네이션 추가  SELECT * FROM jsp.member order by mb_update_at limit 1, 5; {offset}{limit}

		//전체 갯수
		int totalcount = boardService.bdGetAll();
		int contentnum = 10;
		
		
		//데이터 유무 분기
		boolean itemsNotEmpty;
		
		if(totalcount > 0) { // 데이터 있을때
			
			// itemsNotEmpty true일때만, 리스트 & 페이징 보여주기
			itemsNotEmpty = true;
			//페이지 표현 데이터 가져오기
			Map<String,Object> pagination = Pagination.pagination(totalcount, request);
			
			HashMap<String, Object> map = new HashMap<>();
	        map.put("offset",pagination.get("offset"));
	        map.put("contentnum",contentnum);
			
	        //페이지별 데이터 가져오기
			List<BoardListDomain> boardListDomains = boardService.boardAllList(map);
			
			//모델객체 넣어주기
			mav.addObject("itemsNotEmpty", itemsNotEmpty);
			mav.addObject("items", boardListDomains);
			mav.addObject("rowNUM", pagination.get("rowNUM"));
			mav.addObject("pageNum", pagination.get("pageNum"));
			mav.addObject("startpage", pagination.get("startpage"));
			mav.addObject("endpage", pagination.get("endpage"));
			
		}else {
			itemsNotEmpty = false;
		}
		
		return mav;
	}
	
	public ModelAndView Paging(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String page = (String) session.getAttribute("page");
		if(page == null)page = "1";
		session.setAttribute("page", page);
		mav = bdListCall(request);
		mav.setViewName("board/boardList.html");
		return mav;
	}
	}