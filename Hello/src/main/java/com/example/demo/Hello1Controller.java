package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // ①
public class Hello1Controller {

	// http://localhost:8080/hello1
	@GetMapping("/hello1") // ②
	public String sayHello() {

		//return ("Hello, World!");
		return ("みなさん,こんにちは!");

	}

}

/*
①
@RestControllerアノテーション「コントローラークラスのメソッドで処理した結果を、
そのままレスポンスとしてブラウザへ送信する」ことを表すアノテーションです。
本来はJSONやXMLなどを返す「RESTインターフェース」で使うものですが、
「テキストを返す」機能を流用できるので、このアノテーションを利用します
(画面用のアノテーションは後述します)。

②
@GetMappingアノテーション「GETリクエストに対応するメソッド」であることを
表すアノテーションです。「GETリクエスト」は、サーバーへリクエストを
送る方法の１つです。詳しくは3.2節で説明しますが、ブラウザのアドレス欄に
http://localhost:8080/hello1といったURLを入力すると、
サーバーには「GETリクエスト」として送信されます。
@GetMappingは、このGETリクエストを処理するメソッドであることを表しています。
@GetMappingの引数には処理対象とする「URLのパス名」を書きます。
@GetMapping("/hello1")とすると、GETリクエストで/hello1が送られてきたら、
直後にあるsayHello()が自動的に呼び出されるようにしてくれます。
これが先に説明した「URLパス名に対応する処理」になります。
このようなメソッドを「ハンドラーメソッド」と言います。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.82-83).Kindle版.
*/
