package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello3Controller {

	// http://localhost:8080/hello3/James
	@GetMapping("/hello3/{name}") // ①
	public String sayHello(@PathVariable("name") String name) { // ②

		return "Hello,world!" + "こんにちは「" + name + "」さん！";

	}

	// http://localhost:8080/hello3/James/26
	@GetMapping("/hello3/{name}/{age}") // ①
	public String sayHello2(@PathVariable("name") String name, @PathVariable("age") String age) { // ②

		return "Hello,world!" + "こんにちは「" + name + "」さん！" + "年齢は" + age + "歳ですね。";

	}

}

/*
①
@GetMappingアノテーション引数に{}で囲まれた部分があります。
これはURLパスから値として取り出す部分を指定するもので「URIテンプレート変数」と言います。
この場合、URLパスが/hello3/Jamesなら、nameという名前で"James"を取り出せます。

②
@PathVariableアノテーションは、①のURIテンプレート変数の値をメソッド引数にセット(バインド)します。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.100-101).Kindle版.
*/
