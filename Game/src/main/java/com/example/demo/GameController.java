package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller

public class GameController {

	@Autowired // ①
	HttpSession session; // ①

	@GetMapping("/")
	public String index() {

		session.invalidate(); // ② // セッション 内 の 情報 を クリア し ます。 
		// 答えを作って Session に格納
		Random rnd = new Random(); // ③
		int answer = rnd.nextInt(100) + 1; // ③
		session.setAttribute("answer", answer); // ④
		System.out.println("answer=" + answer); // コンソールに正解を出力する(^^)
		return "game"; // (a)

	}

	@PostMapping("/challenge") // ⑤ POSTリクエストでURLパスは/challenge
	public ModelAndView challenge(
		@RequestParam("number") int number, // ⑥ 回答はnumberという名前のパラメータ
		ModelAndView mv
	) {

		// セッションから答えを取得
		int answer = (Integer) session.getAttribute("answer"); // ⑦
		// セッションからユーザーの回答履歴を取得
		@SuppressWarnings("unchecked")
		List<History> histories = (List<History>) session.getAttribute("histories");// ⑧

		if (histories == null) {

			histories = new ArrayList<>();
			session.setAttribute("histories", histories);

		}

		// 判定→回答履歴追加
		if (answer < number) {

			histories.add(new History(histories.size() + 1, number, "もっと小さいです"));// ⑨

		} else if (answer == number) {

			histories.add(new History(histories.size() + 1, number, "正解です！")); // ⑨

		} else {

			histories.add(new History(histories.size() + 1, number, "もっと大きいです"));// ⑨

		}

		mv.setViewName("game"); // ⑩
		mv.addObject("histories", histories); // ⑩
		return mv;

	}

}

/*
①
HttpSession(jakarta.servlet.http.HttpSession)がセッションのクラスです。
変数sessionには、正解や回答履歴を格納して行きます。しかし、sessionを初期化する処理が見当たりません。
このままでは実行時エラー(例外NullPointerException)になるのでは？そう思う方がいるかもしれません。
その答えが@Autowiredアノテーションにあります。通常HttpSessionオブジェクト(=セッション)は、
リクエストを表すHttpServletRequest(jakarta.servlet.http.HttpServletRequest)オブジェクトからgetSession()で取得します。

@Autowiredを付与すると、コントローラークラス起動時、これに相当する処理が自動的に行われ、sessionに設定されます。
つまり自分でセッションのインスタンスをセットする必要がない、というわけです。

この初期化方法は「フィールドインジェクション」と呼ばれています。
以前はよく使われていましたが、現在は「コンストラクタインジェクション」という方法が推奨されています。
後者については次章で説明します。

SpringBootでセッションを操作する場合、@SessionAttributeアノテーションや@Scopeアノテーションを使う方法もあります。
興味がある方は調べてみてください。

②
セッション内の情報をクリアする。

③
java.util.Random#nextInt()は0～引数を超えない範囲でint型の乱数を返します。
nextInt(100)なら、0～99になるので、+1して正解の範囲を1～100とします。

④
第2引数が格納する値(オブジェクト)、第1引数がその名前です。
setAttribute()の第2引数はjava.lang.Object型と定義されているため、
answerはInteger型へAutoboxingされてから、セッションに格納されます。

(a)
最初にアクセスされたとき、コントローラーから画面に渡すデータはありません。
つまりaddObject()は不要です。Thymeleafには、次に表示するビュー名を伝えるだけです。
こういった場合、上記のように文字列でビュー名を指示できます。
これでModelAndView#setViewName()と同じように、templates下にある".html"を付加した名称のファイルが、
次画面としてブラウザへ送信されます。この場合、次画面はgame.htmlとなる。

⑦
セッションから答えを取得。

⑧
セッションからユーザーの回答履歴を取得。

⑨
コメントを省略する。

⑩
最後にビュー名と処理結果(回答履歴)をModelAndViewオブジェクトに設定して、returnします。

菊田英明.SpringBoot3で始めるWebアプリケーション開発入門(基礎編):プログラミング初心者・Webプログラマになりたい人向け超入門テキスト(P.164).Kindle版.
*/