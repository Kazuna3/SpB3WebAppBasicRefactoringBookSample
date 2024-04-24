package com.example.todolist.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.dao.TodoDaoImpl;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoListController {

	private final TodoRepository todoRepository;
	private final TodoService todoService; // Todolist2 で追加
	private final HttpSession session;

	// Todolist5 で追加
	@PersistenceContext // ①
	private EntityManager entityManager;
	TodoDaoImpl todoDaoImpl;

	@PostConstruct // ③
	public void init() {

		todoDaoImpl = new TodoDaoImpl(entityManager);

	}

	@GetMapping("/")
	public String defaultHandlerMethod() {

		return "redirect:/todo";

	}

	// ToDo一覧表示
	@GetMapping("/todo")
	public ModelAndView showTodoList(
		ModelAndView mv,
		@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
	) {

		// sessionから前回の検索条件を取得
		TodoQuery todoQuery = (TodoQuery) session.getAttribute("todoQuery");

		if (todoQuery == null) {

			// なければ初期値を使う
			todoQuery = new TodoQuery();
			session.setAttribute("todoQuery", todoQuery);

		}

		// sessionから前回のpageableを取得
		Pageable prevPageable = (Pageable) session.getAttribute("prevPageable");

		if (prevPageable == null) {

			// なければ@PageableDefaultを使う
			prevPageable = pageable;
			session.setAttribute("prevPageable", prevPageable);

		}

		mv.setViewName("todoList");
		Page<Todo> todoPage = todoDaoImpl.findByCriteria(todoQuery, prevPageable);

		//>>>
		//		// カレントページに出力するデータがゼロ件で、カレントページの前にページがある場合は、カレントページを前ページにする。
		//		List<Todo> pageListData = todoPage.getContent();
		//		System.out.println("pageListData.size() : " + pageListData.size());
		//		System.out.println("prevPageable.getPageNumber() : " + prevPageable.getPageNumber());
		//
		//		if (0 == pageListData.size()) {
		//
		//			prevPageable = prevPageable.previousOrFirst();
		//			todoPage = todoDaoImpl.findByCriteria(todoQuery, prevPageable);
		//
		//			pageListData = todoPage.getContent();
		//			System.out.println("pageListData.size() : " + pageListData.size());
		//			System.out.println("prevPageable.getPageNumber() : " + prevPageable.getPageNumber());
		//
		//		}
		//<<<

		//>>>
		// カレントページに出力するデータがゼロ件で、カレントページの前にページがある場合は、カレントページを前ページにする。
		List<Todo> pageListData = todoPage.getContent();

		if (0 == pageListData.size()) {

			prevPageable = prevPageable.previousOrFirst();
			todoPage = todoDaoImpl.findByCriteria(todoQuery, prevPageable);

		}
		//<<<

		mv.addObject("todoList", todoPage.getContent());
		mv.addObject("todoPage", todoPage);
		mv.addObject("todoQuery", todoQuery);

		return mv;

	}

	// ToDo検索処理
	@PostMapping("/todo/query")
	public ModelAndView queryTodo(
		@ModelAttribute TodoQuery todoQuery,
		BindingResult result,
		@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable,
		ModelAndView mv
	) {

		mv.setViewName("todoList");

		Page<Todo> todoPage = null;

		if (todoService.isValid(todoQuery, result)) {

			// エラーがなければ検索
			// todoPage = todoQueryService.query(todoQuery, pageable);
			// ↓
			// JPQL による検索
			//todoList = todoDaoImpl.findByJPQL(todoQuery);
			todoPage = todoDaoImpl.findByCriteria(todoQuery, pageable);

			// 入力された検索条件を session に保存
			session.setAttribute("todoQuery", todoQuery);

			mv.addObject("todoPage", todoPage);
			mv.addObject("todoList", todoPage.getContent());

		} else {

			// エラーがあった場合
			mv.addObject("todoPage", null);
			mv.addObject("todoList", null);

		}

		// 次行は省略可能となる条件を満たしている為、コメントアウトとした。
		// 条件とは、todoQuery が引数で渡されたオブジェクトであり、且つ、小文字で始まっている事である。
		// mv.addObject("todoQuery", todoQuery);

		return mv;

	}

	// ページリンク押下時
	@GetMapping("/todo/query")
	public ModelAndView queryTodo(
		@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable,
		ModelAndView mv
	) {

		mv.setViewName("todoList");

		// 現在のページ位置を保存
		session.setAttribute("prevPageable", pageable);

		// session に保存されている条件で検索
		TodoQuery todoQuery = (TodoQuery) session.getAttribute("todoQuery");

		Page<Todo> todoPage = todoDaoImpl.findByCriteria(todoQuery, pageable);
		mv.addObject("todoQuery", todoQuery); // 検索条件表示用
		mv.addObject("todoPage", todoPage); // page 情報
		mv.addObject("todoList", todoPage.getContent()); // 検索結果
		return mv;

	}

	// ToDo 入力フォーム表示(Todolist2 で追加)
	// 【処理 1 】 ToDo 一覧画面(todoList.html)で[新規追加]リンクがクリックされたとき
	@PostMapping("/todo/create/form")
	public ModelAndView createTodo(ModelAndView mv) {

		mv.setViewName("todoForm");
		mv.addObject("todoData", new TodoData());
		session.setAttribute("mode", "create");
		return mv;

	}

	// ToDo 追加処理(Todolist2 で追加)
	// 【処理 2 】 ToDo 入力画面(todoForm.html)で[登録]ボタンがクリックされたとき
	// ToDo 追加処理(Todolist2 で追加したものを Todolist3 で改善)
	//// @PostMapping("/todo/create/do")
	@PostMapping("/todo/create")
	public String createTodo(
		@ModelAttribute @Validated TodoData todoData,
		BindingResult result,
		Model model
	) {

		// エラーチェック
		boolean isValid = todoService.isValid(todoData, result);

		if (!result.hasErrors() && isValid) {

			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";

		} else {

			// エラーあり
			// model.addAttribute("todoData", todoData);
			return "todoForm";

		}

	}

	// ToDo 一覧へ戻る(Todolist2 で追加)
	// 【処理 3 】 ToDo 入力画面で[キャンセル登録]ボタンがクリックされたとき
	@PostMapping("/todo/cancel")
	public String cancel() {

		return "redirect:/todo";

	}

	// ToDo表示
	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable(name = "id") int id, ModelAndView mv) {

		mv.setViewName("todoForm");
		Todo todo = todoRepository.findById(id).get();
		mv.addObject("todoData", todo);
		session.setAttribute("mode", "update");
		return mv;

	}

	// ToDo更新処理
	@PostMapping("/todo/update")
	public String updateTodo(
		@ModelAttribute @Validated TodoData todoData,
		BindingResult result,
		Model model
	) {

		// エラーチェック
		boolean isValid = todoService.isValidDateFormat(todoData, result);

		if (!result.hasErrors() && isValid) {

			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";

		} else {

			// エラーあり

			// 次行は省略可能となる条件を満たしている為、コメントアウトとした。
			// 条件とは、todoQuery が引数で渡されたオブジェクトであり、且つ、小文字で始まっている事である。
			// model.addAttribute("todoData", todoData);
			return "todoForm";

		}

	}

	// ToDo削除処理
	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoData todoData) {

		todoRepository.deleteById(todoData.getId());
		return "redirect:/todo";

	}

}
