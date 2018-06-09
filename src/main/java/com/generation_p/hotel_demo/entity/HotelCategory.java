package com.generation_p.hotel_demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
public class HotelCategory extends AbstractEntity implements Serializable, Cloneable {

	@Column(name = "NAME")
	private String categoryName;

	public HotelCategory() {}

	public HotelCategory(Long id, String categoryName) {
		this.setId(id);
		this.categoryName = categoryName;
	}

	public boolean isPersisted() {
		return getId() != null;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "HotelCategory [id=" + getId() + ", categoryName=" + categoryName + "]";
	}

	@Override
	public HotelCategory clone() throws CloneNotSupportedException {
		return (HotelCategory) super.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof HotelCategory))
			return false;

		HotelCategory that = (HotelCategory) o;

		return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
