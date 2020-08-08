package ar.com.thinksoft.jsf;

import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.thinksoft.utils.MessageManager;

public class WrapperNestedDataTable<T> {

	private WrapperNestedDataTable<T> wrapper;
	private List<WrapperNestedDataTable<T>> wrapperList;
	private List<T> list;
	private List<T> filteredList;
	private String label;
	private Object id;
	private boolean reqFooter;
	private boolean seleccionado;

	private Map<Object, List<Object>> data;

	public WrapperNestedDataTable() {
	}

	public void agrupar(List<T> list, List<GrupoNestedTable> grupos, List<GrupoNestedTable> filters) {
		this.list = list;

		wrapperList = new LinkedList<WrapperNestedDataTable<T>>();
		filteredList = new LinkedList<T>();
		if(grupos == null) {
			grupos = new LinkedList<GrupoNestedTable>();
		}
		if(grupos.size() == 0) {
			this.list = new ArrayList<T>();
		}
		try {
			for(T tmp : list) {
				Object idLevel1 = null;
				String labelLevel1 = null;
				if (grupos.size() > 0) {
					idLevel1 = tmp.getClass().getMethod("get" + grupos.get(0).getMethodId()).invoke(tmp);
					labelLevel1 = (String) tmp.getClass().getMethod("get" + grupos.get(0).getMethodLabel()).invoke(tmp);
				}
				Object idLevel2 = null;
				String labelLevel2 = null;
				if (grupos.size() > 1) {
					idLevel2 = tmp.getClass().getMethod("get" + grupos.get(1).getMethodId()).invoke(tmp);
					labelLevel2 = (String) tmp.getClass().getMethod("get" + grupos.get(1).getMethodLabel()).invoke(tmp);
				}

				Object idLevel3 = null;
				String labelLevel3 = null;
				if (grupos.size() > 2) {
					idLevel3 = tmp.getClass().getMethod("get" + grupos.get(2).getMethodId()).invoke(tmp);
					labelLevel3 = (String) tmp.getClass().getMethod("get" + grupos.get(2).getMethodLabel()).invoke(tmp);
				}

				if((grupos.size() >= 1  && idLevel1 == null)
						|| (grupos.size() >= 2 && idLevel2 == null)
						|| (grupos.size() == 3 && idLevel3 == null)) {
					continue;
				}

				if (filters != null) {
					boolean skip = false;
					for (GrupoNestedTable f : filters) {
						String value = (String) tmp.getClass().getMethod("get" + f.getMethodLabel()).invoke(tmp);
						if (f.getMethodId() != null && value != null && !value.toUpperCase().startsWith(f.getMethodId().toUpperCase())) {
							skip = true;
							break;
						}
					}
					if (skip) {
						continue;
					}
				}
				if (grupos.size() == 0) {
					this.list.add(tmp);
				}
				WrapperNestedDataTable<T> w = null;
				if (grupos.size() > 0) {
					for (WrapperNestedDataTable<T> z : wrapperList) {
						if (z.getId().equals(idLevel1)) {
							w = z;
							break;
						}
					}
					if (w == null) {
						w = new WrapperNestedDataTable<T>();
						w.setLabel(labelLevel1);
						w.setId(idLevel1);
						if (grupos.size() > 1) {
							w.setWrapperList(new ArrayList<WrapperNestedDataTable<T>>());
						} else {
							w.setList(new ArrayList<T>());
						}
						w.setReqFooter(grupos.get(0).isReqFooter());
						wrapperList.add(w);
					}
					if (grupos.size() == 1) {
						w.getList().add(tmp);
					}
				}
				WrapperNestedDataTable<T> i = null;
				if (grupos.size() > 1) {
					for (WrapperNestedDataTable<T> z : w.getWrapperList()) {
						if (z.getId().equals(idLevel2)) {
							i = z;
							break;
						}
					}
					if (i == null) {
						i = new WrapperNestedDataTable<T>();
						i.setLabel(labelLevel2);
						i.setId(idLevel2);
						if (grupos.size() == 3) {
							i.setWrapperList(new ArrayList<WrapperNestedDataTable<T>>());
						} else {
							i.setList(new ArrayList<T>());
						}
						w.getWrapperList().add(i);
					}
					if (grupos.size() == 2) {
						i.getList().add(tmp);
					}
				}
				if (grupos.size() == 3) {
					WrapperNestedDataTable<T> l3 = null;
					for (WrapperNestedDataTable<T> z : i.getWrapperList()) {
						if (idLevel3.equals(z.getId())) {
							l3 = z;
							break;
						}
					}
					if (l3 == null) {
						l3 = new WrapperNestedDataTable<T>();
						l3.setLabel(labelLevel3);
						l3.setId(idLevel3);
						l3.setList(new ArrayList<T>());
						i.getWrapperList().add(l3);
					}
					l3.getList().add(tmp);
				}
				filteredList.add(tmp);
			}
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void crossTable(List<T> list, int cols, List<ColumnNestedDataTable> columns, ColumnNestedDataTable colId, List<ColumnNestedDataTable> crossColumn, List<GrupoNestedTable> grupos, List<GrupoNestedTable> filters) {
		this.list = list;

		wrapperList = new LinkedList<WrapperNestedDataTable<T>>();
		filteredList = new LinkedList<T>();
		if(grupos == null) {
			grupos = new LinkedList<GrupoNestedTable>();
		}
		if(grupos.size() == 0) {
			this.list = new ArrayList<T>();
		}
		data = new HashMap<Object, List<Object>>();

		try {
			for(T tmp : list) {
				Object value = tmp.getClass().getMethod("get" + crossColumn.get(0).getId()).invoke(tmp);
				ColumnNestedDataTable col = new ColumnNestedDataTable();
				col.setId(value);
				if (!columns.contains(col)) {
					col.setLabel(tmp.getClass().getMethod("get" + crossColumn.get(0).getLabel()).invoke(tmp).toString());
					col.setMethod(crossColumn.get(0).getMethod());
					columns.add(col);
				}

				value = tmp.getClass().getMethod("get" + colId.getId()).invoke(tmp);
				if (!data.containsKey(value)) {
					data.put(value, new LinkedList<Object>());
				}
				data.get(value).add(tmp);
			}

			System.out.println();
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public WrapperNestedDataTable<T> getWrapper() {
		return wrapper;
	}

	public void setWrapper(WrapperNestedDataTable<T> wrapper) {
		this.wrapper = wrapper;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
		result = prime * result + ((id == null) ? 0 : id.toString().length());
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
		@SuppressWarnings("unchecked")
		WrapperNestedDataTable<T> other = (WrapperNestedDataTable<T>) obj;
		if (!id.equals(other.getId())) {
			System.out.println("(" + id + ") es otro id (" + other.getId() + ")");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WrapperNestedDataTable [wrapper=" + wrapper + ", list=" + list + ", label="
				+ label + ", id=" + id + "]";
	}

	public List<WrapperNestedDataTable<T>> getWrapperList() {
		return wrapperList;
	}

	public void setWrapperList(List<WrapperNestedDataTable<T>> wrapperList) {
		this.wrapperList = wrapperList;
	}

	public boolean isReqFooter() {
		return reqFooter;
	}

	public void setReqFooter(boolean reqFooter) {
		this.reqFooter = reqFooter;
	}

	public String getTotal(String method) {
		if(!list.isEmpty()) {
			try {
				Method m = list.get(0).getClass().getMethod("get" + method);
				Object obj2 = m.invoke(list.get(0));
				NumberFormat nf = null;
				if(obj2 instanceof Double) {
					nf = new DecimalFormat("0.00;(0.00)");
				} else {
					nf = NumberFormat.getInstance();
					nf.setRoundingMode(RoundingMode.FLOOR);
					nf.setMinimumFractionDigits(0);
					nf.setGroupingUsed(false);
				}
				Double ret = 0D;
				for (Object o : list) {
					Method metodo = o.getClass().getMethod("get" + method);
					Object obj = metodo.invoke(o);
					Number n = Number.class.cast(obj);
					ret += n.doubleValue();
				}
				return nf.format(ret);
			} catch (Exception e) {}
		}
		return null;
	}

	public List<T> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<T> filteredList) {
		this.filteredList = filteredList;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

}
