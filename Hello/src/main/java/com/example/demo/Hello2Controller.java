package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello2Controller {

	// http://localhost:8080/hello2?name=James
	@GetMapping("/hello2")
	public String sayHello(@RequestParam("name") String name) { // ①

		return "Hello,world!" + "こんにちは" + name + "";

	}

}

/*
①
@RequestParamは引数で指定されたパラメータの値を、メソッド実行前にクエリ文字列から取得し、メソッド引数へセット(バインド)します。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.96).Kindle版.
*/
