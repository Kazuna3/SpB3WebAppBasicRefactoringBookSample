package com.example.demo;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

	@PostMapping("/register1")
	public ModelAndView register(
		@RequestParam("name") String name,
		@RequestParam("password") String password,
		// @RequestParam("gender") int gender,  // int 型では、未入力の場合、null を値として格納できない為、エラーが発生する。
		@RequestParam("gender") Integer gender, // int 型のラッパークラスにすると、エラーは発生しない。
		@RequestParam("area") int area,
		// @RequestParam("interest") int[] interest,  // 上の int 型の gender と同じ理由で、未選択の場合はエラーが発生する。
		@RequestParam("interest") Integer[] interest, // int 型のラッパークラスの配列にすると、未選択であってもエラーは発生しない。
		@RequestParam("remarks") String remarks,
		ModelAndView mv
	) {

		StringBuilder sb = new StringBuilder();

		sb.append("名前：" + name);
		sb.append("、パスワード：" + password);
		sb.append("、性別：" + gender);
		sb.append("、地域：" + area);
		sb.append("、興味のある分野：" + Arrays.toString(interest));
		sb.append("、備考：" + remarks.replaceAll("\n", ""));

		System.out.println("sb.toString():" + sb.toString());

		mv.setViewName("result");
		mv.addObject("registData", sb.toString());

		return mv;

	}

	@PostMapping("/register2")
	public ModelAndView register(
		@ModelAttribute RegistData registData, // ①
		ModelAndView mv
	) {

		StringBuilder sb = new StringBuilder();

		sb.append("氏名：" + registData.getName());
		sb.append(", パスワード：" + registData.getPassword());
		sb.append(", 性別：" + registData.getGender());
		sb.append(", 地域：" + registData.getArea());
		sb.append(", 興味のある分野：" + Arrays.toString(registData.getInterest()));

		//改行文字を空文字に置換してもしなくても、改行文字を含むデータの出力結果は同じだった。
		//sb.append(", 備考：" + registData.getRemarks());
		sb.append(", 備考：" + registData.getRemarks().replaceAll("\n", ""));

		mv.setViewName("result");
		mv.addObject("registData", sb);

		return mv;

	}

}
