package cn.appsys.tools;

public class Page {

	private int pageSize;  //页面大小，即每一页显示的记录数。  5代表每一页显示5条记录
	private int currPageNo;  //当前页码 
	private int totalPageCount;  //总的页数        total总共  page是页   count计数
	private int totalCount;  //总的记录数
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPageNo() {
		return currPageNo;
	}
	public void setCurrPageNo(int currPageNo) {
		this.currPageNo = currPageNo;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	//在总记录数赋值时，根据每页显示的记录数与总的记录数得到总的页数
	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
			
			 // 根据【总的记录数】和【每页显示的数据行数】计算【总的页数】
			this.totalPageCount = totalCount % pageSize == 0 ? totalCount / pageSize
					: totalCount / pageSize + 1;
		}
	}
}
