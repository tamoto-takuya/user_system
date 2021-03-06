package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BranchService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final UserService userService;

	@Autowired
	private final BranchService branchService;

	@Autowired
	private final PostService postService;

	//user一覧表示
	@GetMapping
	public String index(Model model) {
		List<User> users = userService.findAll();
		model.addAttribute("users" ,users);
		return "index";
	}

	//新規登録画面遷移
	@GetMapping(value = "/signup")
	public String newUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("branchlist", branchService.findAll());
		model.addAttribute("postlist", postService.findAll());
		return "/signup";
	}

	//ユーザー編集画面遷移
	@GetMapping(value = "/{id}/update")
	public String update(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("user", userService.findById(id));
		model.addAttribute("branchlist", branchService.findAll());
		model.addAttribute("postlist", postService.findAll());
		return "/update";
	}

	//新規登録実行
	@PostMapping(value = "/signup")
	public String signup(Model model, @Validated @ModelAttribute("user") User user, BindingResult result,
			@ModelAttribute("post") Integer post,@ModelAttribute("branch") Integer branch,
			@ModelAttribute("confirmPass") String confirmPass) {

		if (result.hasErrors()) {
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/signup";
		}
		//findByIdloginidで同じログインIDをリストに格納。1つでも同じものがあればエラー表示
		List<User> exitsLoginId = userService.findByLoginIdContaining(user.getLoginId());
		if (exitsLoginId.size()!= 0) {
			model.addAttribute("loginIdMessage", "ログインIDが存在します！！");
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/signup";
		}
		if (StringUtils.isEmpty(user.getPassword()) == true) {
			model.addAttribute("passMessage", "パスワードを入力してください");
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/signup";
		} else {
			if (!user.getPassword().matches("[a-zA-Z0-9!-/:-@\\[-`{-~]{6,20}")) {
				model.addAttribute("passMessage", "パスワード半角英数字6文字以上20文字以下で入力してください！");
				model.addAttribute("branchlist", branchService.findAll());
				model.addAttribute("postlist", postService.findAll());
				return "/signup";
			}
			if (!user.getPassword().equals(confirmPass)) {
				model.addAttribute("confirmPassMessage", "パスワードが一致しません！");
				model.addAttribute("branchlist", branchService.findAll());
				model.addAttribute("postlist", postService.findAll());
				return "/signup";
			}
		}
		if (branch == 0) {
			model.addAttribute("branchMessage", "所属を選んでください！！");
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/signup";
		}
		if (post == 0) {
			model.addAttribute("branchMessage", "役職を選んでください！！");
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/signup";
		}
		//パスワード暗号化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		userService.save(user);
		return "redirect:/users";
	}

	//ユーザー編集更新実行
	@PostMapping(value = "/{id}/update")
	public String update(@ModelAttribute("confirmPass") String confirmPass,
			Model model, @Validated  @ModelAttribute("user") User user, BindingResult result) {

		//findByIdOriginalで同じログインIDをリストに格納。同じものがあればエラー表示
		List<User> exitsLoginId = userRepository.findByIdOriginal(user.getLoginId(), user.getId());
		if (exitsLoginId.size() != 0) {
			model.addAttribute("loginIdMessage", "ログインIDが存在します！！");
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/update";
		}

		if (result.hasErrors()) {
			model.addAttribute("branchlist", branchService.findAll());
			model.addAttribute("postlist", postService.findAll());
			return "/update";
		}

		if (StringUtils.isEmpty(user.getPassword()) == true) {
			User userUpdate = userService.findById(user.getId());
			userUpdate.setLoginId(user.getLoginId());
			userUpdate.setName(user.getName());
			userUpdate.setBranch(user.getBranch());
			userUpdate.setPost(user.getPost());
			userService.save(userUpdate);
			return "redirect:/users";
		} else {
			if (!user.getPassword().equals(confirmPass)) {
				model.addAttribute("confirmPassMessage", "パスワードが一致しません！");
				model.addAttribute("branchlist", branchService.findAll());
				model.addAttribute("postlist", postService.findAll());
				return "/update";
			}
			if (!user.getPassword().matches("[a-zA-Z0-9!-/:-@\\[-`{-~]{6,20}")) {
				model.addAttribute("passMessage", "パスワード半角英数字6文字以上20文字以下で入力してください！");
				model.addAttribute("branchlist", branchService.findAll());
				model.addAttribute("postlist", postService.findAll());
				return "/update";
			}
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		userService.save(user);
		return "redirect:/users";
	}

	//復活停止
	@PostMapping
	public String statusUpdate(@RequestParam("id") Integer id, @RequestParam ("is_active") Integer is_active)  {
		User userIsActive = userService.findById(id);
		userIsActive.setIs_active(is_active);
		userService.save(userIsActive);
		return "redirect:/users";
	}

	//削除機能
	@DeleteMapping("/{id}")
	public String destroy(@PathVariable Integer id) {
		userService.deleteById(id);
		return "redirect:/users";
	}
}
