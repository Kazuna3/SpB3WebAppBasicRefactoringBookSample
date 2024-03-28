package com.example.demo;

import lombok.Data;

@Data // ①
public class RegistData {

	private String name;
	private String password;
	private int gender;
	private int area;
	private int[] interest;
	private String remarks;

}

/*
本クラスは、input.htmlのフォームの内容を保持するためのものである。
フォームの内容を保持するクラスは、フォームクラスと呼ばれる。

①は、Lombokの@Dataアノテーションである。
このアノテーションを付与すると、フィールドのsetter,getterメソッドを自動生成する。
また、デフォルトコンストラクタやtoString()などのメソッドも、合わせて自動生成する。
Lombokはソースコードを変えず、直接classファイルを操作する。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.142).Kindle版.
 */
