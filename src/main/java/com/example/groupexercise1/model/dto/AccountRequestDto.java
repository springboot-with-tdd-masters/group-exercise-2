package com.example.groupexercise1.model.dto;

import java.util.Objects;

public class AccountRequestDto {

	private long id;
	private String name;
	private String type;

	public AccountRequestDto() {
	}

	public AccountRequestDto(long id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AccountRequestDto that = (AccountRequestDto) o;
		return id == that.id && Objects.equals(name, that.name) && Objects.equals(
				type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, type);
	}
}
