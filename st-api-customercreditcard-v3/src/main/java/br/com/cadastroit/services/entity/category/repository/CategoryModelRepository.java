package br.com.cadastroit.services.entity.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cadastroit.services.entity.category.model.CategoryModel;

public interface CategoryModelRepository extends JpaRepository<CategoryModel, Long>{}