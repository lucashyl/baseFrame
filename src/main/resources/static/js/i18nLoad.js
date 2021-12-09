document.write("<script language=javascript src='/static/js/jquery.min.js'></script>");
document.write("<script language=javascript src='/static/js/jquery.i18n.js'></script>");
function  set_properties(local) {
	$.i18n.properties({
		name: 'messages', // 资源文件名称
		// path:  baseUrl+"/sport/member/queryMessagesLanguage?languageType="+local,
		path:  baseUrl+'/static/i18n/', // 资源文件所在目录路径
		mode: 'map', // Map的方式使用资源文件中的Key
		language: local, // 设置的语言
		cache: true,
		encoding: 'UTF-8',
		callback:function (){
			try {
				//初始化页面元素
				$('[data-i18n-placeholder]').each(function () {
					$(this).attr('placeholder', $.i18n.prop($(this).data('i18n-placeholder')));
				});
				$('[data-i18n-text]').each(function () {
					//如果text里面还有html需要过滤掉
					var html = $(this).html();
					var reg = /<(.*)>/;
					if (reg.test(html)) {
						var htmlValue = reg.exec(html)[0];
						$(this).html(htmlValue + $.i18n.prop($(this).data('i18n-text')));
					}
					else {
						$(this).text($.i18n.prop($(this).data('i18n-text')));
					}
				});
				$('[data-i18n-value]').each(function () {
					$(this).val($.i18n.prop($(this).data('i18n-value')));
				});
				$('[data-i18n-lay-text]').each(function () {
					$(this)[0].attributes['lay-text'].nodeValue=$.i18n.prop($(this).data('i18n-lay-text'));
				});
				$("[data-i18n-title]").each(function (e) {
					$(this).attr("title",$.i18n.prop($(this).data('i18n-title')));
				})
				// $("[lang-pla]").each(function (e) {
				// 	$(this).attr("placeholder",$.i18n.prop('lang_pla_'+$(this).attr("lang-pla")));
				// })
				// $("[lang-ht]").each(function (e) {
				// 	console.log($.i18n.prop('lang_ht_'+$(this).attr("lang-ht")));
				// 	$(this).html($.i18n.prop('lang_ht_'+$(this).attr("lang-ht")));
				// })
			}
			catch(ex){ console.info("i18n转换异常")}
		}
	});
}
document.write("<script>set_properties(language)</script>");