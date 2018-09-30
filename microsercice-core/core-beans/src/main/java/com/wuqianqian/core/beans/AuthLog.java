package com.wuqianqian.core.beans;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AuthLog implements Serializable {

	private static final long	serialVersionUID	= -7612739305546935933L;

	private Log					log;

}
