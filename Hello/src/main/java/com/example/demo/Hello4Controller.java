package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello4Controller {

	@PostMapping("/hello4") // ①
	public String sayHello(@RequestParam("name") String name) { // ②

		return "Hello,world!" + "こんにちは「" + name + "」さん！";

	}

}

/*
①
ハンドラーメソッドに付与されている@PostMappingアノテーションは、「POSTリクエストを処理するメソッド」ということを表しています。

②
フォーム部品に入力された値は、GETリクエストと同じくクエリ文字列形式(パラメータ名=パラメータ値)のため、
@RequestParamで受け取ります。これでPOSTリクエストによって送られたフォームの入力値を取得できるようになります。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.113).Kindle版.
*/
