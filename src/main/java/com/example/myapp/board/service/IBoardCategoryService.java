package com.example.myapp.board.service;

import java.util.List;

import com.example.myapp.board.model.BoardCategory;

public interface IBoardCategoryService {
	List<BoardCategory> selectAllCategory();
	void insertNewCategory(BoardCategory boardCategory);
	void updateCatgeory(BoardCategory boardCategory);
	void deleteCategory(int categoryId);
}
