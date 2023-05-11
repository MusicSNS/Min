package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "builder")
public class BoardCommentListDomain {
	
	private String bcSeq;
	private String bdSeq;
	private String mbId;
	private String mbName;
	private String bcContent;
	private String bcCreateAt;
	private String bcUpdateAt;
	
}
