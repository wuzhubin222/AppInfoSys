function page_nav(frm,num){
		// frm 当前页面中的某个表单对象， 
	   //currPageNo是当前表单中，某个表单组件的name属性值。
	   //frm.currPageNo 得到所对应的表单组件。
		frm.currPageNo.value = num;
		frm.submit();
}
