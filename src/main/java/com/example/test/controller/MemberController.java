package com.example.test.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.model.Member;
import com.example.test.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping
	public List<Member> getAllMembers(){
		return memberService.getAllMembers();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Member> getMemberById(@PathVariable Long id){
		Optional<Member> member = memberService.getMemberById(id);
		return member
				.map(ResponseEntity::ok)
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Member createMember(@RequestBody Member member) {
		return memberService.createMember(member);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member memberDetails){
		Optional<Member> updateMember = memberService.updateMember(id, memberDetails);
		return updateMember
				.map(ResponseEntity::ok)
				.orElseGet(()->ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletMember(@PathVariable Long id){
		if(memberService.deleteMember(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
