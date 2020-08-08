package ar.com.thinksoft.jsf;

public class ColumnNestedDataTable {

	private String width;
	private String label;
	private String method;
	private String columnClass;
	private boolean checkbox;
	private boolean checkboxDisabled;
	private String converter;
	private int row;
	private int colspan;
	private int rowspan;
	private boolean totalizar;
	private String footerLabel;
	private Object id;

	public ColumnNestedDataTable() {
		row = 1;
		colspan = 1;
		rowspan = 1;
	}

	public ColumnNestedDataTable(String label, String method, String width) {
		this.label = label;
		this.width = width;
		this.method = method;
		row = 1;
		colspan = 1;
		rowspan = 1;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getColumnClass() {
		return columnClass;
	}

	public void setColumnClass(String columnClass) {
		this.columnClass = columnClass;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public boolean isTotalizar() {
		return totalizar;
	}

	public void setTotalizar(boolean totalizar) {
		this.totalizar = totalizar;
	}

	public String getFooterLabel() {
		return footerLabel;
	}

	public void setFooterLabel(String footerLabel) {
		this.footerLabel = footerLabel;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hashCode();
		result = prime * result + ((id == null) ? 0 : new Long(id.toString()).intValue());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			System.out.println("es null");
			return false;
		}
		if (getClass() != obj.getClass()) {
			System.out.println("(" + getClass() + ") es otra clase(" + obj.getClass() + ") ");
			return false;
		}
		ColumnNestedDataTable other = (ColumnNestedDataTable) obj;
		if (!id.equals(other.getId())) {
			System.out.println("(" + id + ") es otro id (" + other.getId() + ")");
			return false;
		}
		return true;
	}

	public boolean isCheckboxDisabled() {
		return checkboxDisabled;
	}

	public void setCheckboxDisabled(boolean checkboxDisabled) {
		this.checkboxDisabled = checkboxDisabled;
	}

}
