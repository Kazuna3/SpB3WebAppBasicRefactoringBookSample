package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // Lombokのアノテーション。全フィールドに値をセットするコンストラクタを自動生成する。 
@Getter // Lombokのアノテーション。フィールドに対するgetterメソッドを自動生成する。
public class History {

	private int seq;
	private int yourAnswer;
	private String result;

}
