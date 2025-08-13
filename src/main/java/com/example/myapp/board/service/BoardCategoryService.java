package com.example.myapp.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.board.dao.IBoardCategoryRepository;
import com.example.myapp.board.model.BoardCategory;

@Service
public class BoardCategoryService implements IBoardCategoryService{

	@Autowired
	IBoardCategoryRepository boardCategoryRepository;
	
	@Override
	public List<BoardCategory> selectAllCategory() {
		
		return boardCategoryRepository.selectAllCategory();
	}

	@Override
	public void insertNewCategory(BoardCategory boardCategory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCatgeory(BoardCategory boardCategory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCategory(int categoryId) {
		// TODO Auto-generated method stub
		
	}
	
	
}
