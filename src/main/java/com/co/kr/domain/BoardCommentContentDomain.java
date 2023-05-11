package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class BoardCommentContentDomain {

	private Integer bcSeq;
	private String bdSeq;
	private String mbId;
	private String mbName;
	private String bcContent;
	
}