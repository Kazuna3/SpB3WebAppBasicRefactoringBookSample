package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller // ①
public class Hello5Controller {

	// http://localhost:8080/hello5?name=James
	@GetMapping("/hello5")
	public ModelAndView sayHello(
		@RequestParam("name") String name,
		ModelAndView mv // ②
	) {

		mv.setViewName("hello55"); // ③
		mv.addObject("name", name); // ④
		return mv; // ⑤

	}

}

/*
①
@Controllerアノテーション前章では@RestControllerをクラスに付与しましたが、@Controllerに変わっています。
@RestControllはテキスト用のものでした。一方、ここではThymeleafが処理したテンプレート(HTML)を返します。
こういった場合は、@Controllerを使います。

②
ModelAndViewオブジェクトメソッド引数にModelAndViewオブジェクトが追加されています。
またメソッドの戻り値もModelAndView型です。このModelAndViewは、
その名の通り「モデル」と「ビュー名」を保持するクラスです。

・ビュー名：次に表示する画面名
・モデル：ビュー(画面)で使用するデータ

③
ビュー名はModelAndView#setViewName()で設定します。
Thymeleafは設定されたビュー名に拡張子".html"を追加したファイルをsrc/main/resources/templates下から探します。
よって③は、次に表示する画面としてhello55.htmlを指定したことになります。

④
次のModelAndView#addObject()は、ビューが使うデータを渡します。
第2引数がビューに渡すデータ(オブジェクト)です。
第1引数はその名前です。
少々紛らわしいのですが、hello55.htmlに記述したth:text="${name}"のnameに対応するのは、第1引数の方です。

名前を以下のように変えてもかまいません。この場合は、ビュー側も第1引数に合わせます。

mv.addObject("onamae",name);
↓
<spanth:text="${onamae}"></span>

⑤
最後にビュー名とモデルをセットしたmvをreturnします。
mvはThymeleafに渡され、前述の処理が実行されます。そしてブラウザには、入力された名前を含むhello55.htmlが返されます。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.127).Kindle版.
*/
