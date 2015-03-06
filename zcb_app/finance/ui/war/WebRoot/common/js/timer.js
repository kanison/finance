var TFL = TFL || {};
TFL.timer = (
function()
{
	var _re = /\-|\_|\.|\*|\/|\\|[\u4e00-\u9fa5]/g;
	var _splitStr = "-";//显示分隔符
	var _this =
	{
		obj : null,
		curControl : null,
		curIndex : 0,
		year : new Date().getFullYear(),//定义年的变量的初始值
		month : new Date().getMonth() + 1,//定义月的变量的初始值
		days : new Array(37),//定义写日期的数组
		speMonth : [31,28,31,30,31,30,31,31,30,31,30,31]//定义每个月的最大天数
	};
	var _$ = function(o)
	{
		return typeof o == "object" ? o : document.getElementById(o);
	};
	var _isIE = (document.all) ? true : false;
    var _isIE6 = _isIE && ([/MSIE (\d)\.0/i.exec(navigator.userAgent)][0][1] == 6);
	/**
	 * 绑定文本框
	 */
	var _bindControl = function(obj)
	{
		/**
		 * obj属性
		 * 1.showControl(true || false) 是否显示控件
		 * 2.parent 控件显示的容器
		 * 3.items 控件id名集合,如:["input1","input2"]
		 * 4.rules 控件显示规则
		 * 5.today 今天链接{"hidden" : 隐藏，默认显示}
		 * 6.mask {true : 显示iframe解决ie6下select问题，默认不显示}
		 * 7.evn 数组变量[]，日期点击时触发
		 * 8.autoShow 是否自动显示下一个控件
         * 9.offsetX 与当前对象的水平偏移值    add by carlli
         * 10. offsetY 与当前对象的垂直偏移值  add by carlli
		 */
		var _o = _this.obj = obj;
		if(_o.showControl && _o.parent)//满足这两个条件则自动画控件
		{
			for(var i = 0; i < _o.items.length; i++)
			{
				var _i = document.createElement("input");
				_i.id = _o.items[i];
				_i.index = i;
				_i.style.imeMode = "disabled"
				_bindClickEvt(_i);
				_$(_o.parent).appendChild(_i);
				if(i < _o.items.length - 1)
				{
					var _span = document.createElement("span");
					_span.innerHTML = " 至 ";
					_$(_o.parent).appendChild(_span);
				}
			}
		}
		else
		{
			for(var i = 0; i < _o.items.length; i++)
			{
				_$(_o.items[i]).index = i;
				_$(_o.items[i]).style.imeMode = "disabled"
				_bindClickEvt(_$(_o.items[i]));
			}
		}
	};
	var _bindClickEvt = function(ctl)
	{
		if(document.all)
		{
			ctl.attachEvent(
				"onfocus",
				function(e)
				{
					_this.curControl = ctl;
					_this.curIndex = ctl.index;
					_showTimer();
					_clearEvent(e);
				}
			);
		}
		else
		{
			ctl.addEventListener(
				"focus",
				function(e)
				{
					_this.curControl = ctl;
					_this.curIndex = ctl.index;
					_showTimer();
					_clearEvent(e);
				},false
			);
		}
		ctl.onmousedown = _clearEvent;
	};
	var _getToday = function()
	{
        var cMonth = (new Date().getMonth() + 1);
        var cDay = new Date().getDate();
        cMonth = (cMonth < 10) ? '0' + cMonth : cMonth;
        cDay = (cDay < 10) ? '0' + cDay : cDay;
		return new Date().getFullYear() + _splitStr + cMonth + _splitStr + cDay;
	};
	/**
	 * 显示日期控件
	 */
	var _showTimer = function()
	{
		if(!_$("dvTimerContainer")) return false;
		var _control = _this.curControl;
		if(_this.obj.today && _this.obj.today == "hidden")
		{
			_$("todayDate").style.display = "none";
		}
		else
		{
			_$("todayDate").style.display = "";
		}
		if(typeof(_this.obj.rules) == "object")
		{
			if(_this.obj.rules.from && _this.curIndex == 0)
			{
				_this.year = parseInt(_this.obj.rules.from.split(_re)[0],10);
				_this.month = parseInt(_this.obj.rules.from.split(_re)[1],10);
			}
			if(!_this.obj.rules.end) _this.obj.rules.end = _getToday();
		}
		if(timeFormat(_control.value))//如果控件内容符合日期格式则自动定位到当前年月
		{
			_this.year = parseInt(_control.value.split(_re)[0],10);
			_this.month = parseInt(_control.value.split(_re)[1],10);
		}
		_initYearList(_this.year);
	  	_initMonthList(_this.month);
	  	_initDate(_this.year,_this.month);
	  	var _controlStyle  = _$("dvTimerContainer").style;
	  	var _top = _control.offsetTop;     //TT控件的定位点高
	  	var _height = _control.clientHeight;  //TT控件本身的高
	  	var _left = _control.offsetLeft;    //TT控件的定位点宽
	  	var _type = _control.type;          //TT控件的类型
	  	while (_control = _control.offsetParent){_top+=_control.offsetTop; _left += _control.offsetLeft;}
	 	if(typeof(_this.obj.offsetY) == "number"){
            _controlStyle.top = (_top + _this.obj.offsetY) + "px";
        }else{
            _controlStyle.top = (_type=="image")? _top+_height : _top + _height + 5 + "px";
        }
        if(typeof(_this.obj.offsetX) == "number"){
            _controlStyle.left = (_left + _this.obj.offsetX) + "px";
        }else{
	 	    _controlStyle.left = _left + "px";
        }
	  	_controlStyle.display = "";
	};
	/**
	 * 将日前赋值给当前文本框，当有下一个文本框时则自动移动到下一个文本框，否则隐藏文本框
	 */
	var _setTime = function()
	{
		_this.curControl.value = _this.year + _splitStr + (Number(_this.month) < 10 ? '0' + Number(_this.month) : Number(_this.month)) + _splitStr + (this.index < 10 ? '0' + this.index : this.index);
		if(_this.obj.evn && typeof(_this.obj.evn[_this.curIndex]) == "function")
		{
			_this.obj.evn[_this.curIndex].call(null); //触发点击事件
		}
		if(_this.curIndex < _this.obj.items.length - 1)
		{
			if(typeof(_this.obj.autoShow) != "undefined" && _this.obj.autoShow == false)
			{
				_$("dvTimerContainer").style.display = "none";
				return;
			}
			_this.curIndex += 1;
			_this.curControl = _$(_this.obj.items[_this.curIndex]);
			_showTimer();
		}
		else
		{
			_$("dvTimerContainer").style.display = "none";
		}
	};
	/**
	 * 初始化月份下拉
	 */
	var _initMonthList = function(m)
	{
		var m = m || new Date().getMonth() + 1;
		var _select = document.createElement("select");
		_select.onchange = function()
		{
			_this.month = this.value;
			_initDate(_this.year,_this.month);
		};
	 	for (var i = 1; i < 13; i++)
	 	{
	 		var _opt = document.createElement("option");
	    	if(i == m)
	       	{
	       		_opt.value = i;
	       		_opt.selected = true;
	       		_opt.innerHTML = _getMonCN(i) + "月";
	       	}
	    	else
	    	{
	    		_opt.value = i;
	       		_opt.innerHTML = _getMonCN(i) + "月";
	    	}
	    	_select.appendChild(_opt);
	  	}
	  	_$("snTimerMonthList").innerHTML = "";
	  	_$("snTimerMonthList").appendChild(_select);
	};
	var _getMonCN = function(m)
	{
		var _rtn = "";
		var _list = ["一","二","三","四","五","六","七","八","九","十","十一","十二"];
		for(var i = 1; i < _list.length + 1; i++)
		{
			if(m == i)
			{
				_rtn = _list[i - 1];
				break;
			}
		}
		return _rtn;
	};
	/**
	 * 初始化年份下拉
	 */
	var _initYearList = function(y)
	{
		var m = y || new Date().getFullYear();
	    if (m < 1000 || m > 9999) {alert("年份值不在 1000 到 9999 之间！");return;}
	  	var n = m - 10;
	  	if (n < 1000) n = 1000;
	  	if (n + 25 > 9999) n = 9974;

	  	var x = n;
	  	var y = n + 25;

	  	var _select = document.createElement("select");
	  	_select.onchange = function()
		{
			_this.year = this.value;
			_initDate(_this.year,_this.month);
		};
		if(_this.obj.rules)
	  	{
	  		if(_this.obj.rules.from) x = parseInt(_this.obj.rules.from,10);
			if(_this.obj.rules.end) y = parseInt(_this.obj.rules.end,10);
	  	}
	  	for (var i = x; i <= y; i++)
		{
		  	var _opt = document.createElement("option");
		    if(i == m)
		    {
		    	_opt.value = i;
		    	_opt.selected = true;
		    	_opt.innerHTML = i;
		    }
		    else
		    {
		    	_opt.value = i;
		       	_opt.innerHTML = i;
		    }
		    _select.appendChild(_opt);
		 }
	  	_$("snTimerYearList").innerHTML = "";
	  	_$("snTimerYearList").appendChild(_select);
	};
	/**
	 * 判断是否闰年
	 */
	var _isLeapYear = function(y)
	{
		if(0 == y % 4 && ((y % 100 != 0) || (y % 400 == 0)))
			return true;
		else
			return false;
	};
	/**
	 * 取得当月天数
	 */
	var _getMonDays = function(y,m)
	{
		var c = _this.speMonth[m - 1];
		if((m == 2) && _isLeapYear(y)) c++;
		return c;
	};
	/**
	 * 清除冒泡事件
	 */
	var _clearEvent = function(evt)
	{
		var e = (evt) ? evt : window.event;
		if (window.event)
		{
			e.cancelBubble=true;
		}
		else
		{
			e.stopPropagation();
		}
	};
	/*翻月*/
	var _turnMonth = function(flag){
		var _turnDate = new Date();
		_turnDate.setFullYear(_this.year);
		_turnDate.setMonth(_this.month - 1);
		_turnDate.setDate("1");
		if(flag){
			_turnDate.setMonth(_this.month - 2);
			_this.year = _turnDate.getFullYear();
			_this.month = _turnDate.getMonth() + 1;
		}else{
			_turnDate.setMonth(_this.month);
			_this.year = _turnDate.getFullYear();
			_this.month = _turnDate.getMonth() + 1;
		}
		_initYearList(_this.year);
		_initMonthList(_this.month);
		_initDate(_this.year,_this.month);
	}
	/**
	 * 初始化日期
	 */
	var _initDate = function(yy,mm)
	{
		for(var i = 0; i < 37; i++){_this.days[i] = ""};  //将显示框的内容全部清空
	  	var day1 = 1,firstday = new Date(yy,mm-1,1).getDay();  //某月第一天的星期几
	 	for(var i = firstday; day1 < _getMonDays(yy,mm)+1; i++){_this.days[i]=day1;day1++;}
		var _last =
		{
			control : null,
			year : "",
			month : "",
			day : ""
		};
		if(_this.obj.items.length > 1)
		{
			if(_this.curIndex > 0)
			{
				_last.control = _this.obj.items[_this.curIndex - 1];
				_last.year = _$(_last.control).value.split(_re)[0];
				_last.month = _$(_last.control).value.split(_re)[1];
				_last.day = _$(_last.control).value.split(_re)[2];
			}
		}
	  	for(var i = 0; i < 37; i++)
	  	{
	  		var da =_$("timerItem" + i);//书写新的一个月的日期星期排列
	  		var isToday = (yy == new Date().getFullYear() && mm == new Date().getMonth()+1 && _this.days[i] == new Date().getDate());
	   		if (_this.days[i] != "")
	      	{
	      		da.innerHTML = _this.days[i];
		        da.className = isToday ? "select-day" : "other-day";
				var _disabled = true;
				if(_last.control != null)
				{
					if(parseInt(_this.year) < parseInt(_last.year)) _disabled = false;
					if(parseInt(_this.year) == parseInt(_last.year) && parseInt(_this.month) < parseInt(_last.month)) _disabled = false;
					if(parseInt(_this.year) == parseInt(_last.year) && parseInt(_this.month) == parseInt(_last.month) && parseInt(da.innerHTML) < parseInt(_last.day)) _disabled = false;
				}
				/*判断规则日期*/
				if(typeof _this.obj.rules == "object" && _disabled)
				{
					if(_this.obj.rules.from)
					{
						var _fromYear = _this.obj.rules.from.split(_re)[0];
						var _fromMonth = _this.obj.rules.from.split(_re)[1];
						var _fromDay = _this.obj.rules.from.split(_re)[2];
						var _endYear = _this.obj.rules.end.split(_re)[0];
						var _endMonth = _this.obj.rules.end.split(_re)[1];
						var _endDay = _this.obj.rules.end.split(_re)[2];
						if(parseInt(_this.year) < parseInt(_fromYear) || parseInt(_this.year) > parseInt(_endYear)) _disabled = false;
						if((parseInt(_this.year) == parseInt(_fromYear) && parseInt(_this.month) < parseInt(_fromMonth,10)) || (parseInt(_this.year) == parseInt(_endYear) && parseInt(_this.month) > parseInt(_endMonth,10))) _disabled = false;
						if((parseInt(_this.year) == parseInt(_fromYear) && parseInt(_this.month) == parseInt(_fromMonth,10) && parseInt(da.innerHTML) < parseInt(_fromDay,10)) || (parseInt(_this.year) == parseInt(_endYear) && parseInt(_this.month) == parseInt(_endMonth,10) && parseInt(da.innerHTML) > parseInt(_endDay,10))) _disabled = false;
					}
				}

				if(_disabled)
				{
					da.style.cursor="pointer";
					da.onclick = _setTime;
					da.index = _this.days[i];
					if(!isToday)
					{
						da.style.backgroundColor = "#fff";
						da.style.color = "#07679C";
						da.onmouseover = function()
						{
							this.style.backgroundColor = "#07679C";
							this.style.color = "#ffffff";
						};
						da.onmouseout = function()
						{
							this.style.backgroundColor = "";
							this.style.color = "#07679C";
						};

						/*if(timeFormat(_this.curControl.value))
						{
							if(_this.curControl.value.split(_re)[2] == da.index)
							{
								da.style.backgroundColor = "#07679C";
								da.style.color = "#fff";
							}
						}*/
					}
					else
					{
						da.style.color = "#fff";
						da.style.backgroundColor = "#043C59";
						da.onmouseover = null;
   						da.onmouseout = null;
					}
				}
				else
				{
					da.style.cursor = "default";
					da.style.color = "#999";
					da.style.backgroundColor = "#fff";
   					if(da.onclick) da.onclick = null;
   					da.onmouseover = null;
   					da.onmouseout = null;
				}
	      	}
	    	else
	    	{
				da.className = "";
	    		da.innerHTML = "";
	    		da.style.backgroundColor = "#fff";
	    		da.style.cursor = "default";
				da.onmouseover = null;
   				da.onmouseout = null;
	    	}
	  	}
	};
	/**
	 * 初始化时间控件
	 */
	var _init = function()
	{
		var _p = document.documentElement.firstChild;
		var _c = document.createElement("link");
		_c.type = "text/css";
		_c.href = "https://www.tenpay.com/v2.0/time/time.css?v=20091221";
		_c.rel = "stylesheet";
		_p.appendChild(_c);

		var _dv = document.createElement("div");
		_dv.id = "dvTimerMain";
		_dv.style.cssText = "border:1px solid #9DB0BC; width:178px; text-align:center;";

		var _dv2 = document.createElement("div");
		_dv2.className = "date-select";
		var _sn_prev = document.createElement("ins");
		_sn_prev.title = "上个月";
		_sn_prev.onclick = function(){_turnMonth(true)};
        _sn_prev.className = "time-arrow";
		var _sn_next = document.createElement("ins");
		_sn_next.title = "下个月";
		_sn_next.onclick = function(){_turnMonth()};
        _sn_next.className = "time-arrow";
		_sn_prev.innerHTML = "&lt;&lt;";
		_sn_next.innerHTML = "&gt;&gt;";
		var _sn1 = document.createElement("span");
		var _sn2 = document.createElement("span");
		_sn1.id = "snTimerMonthList";
		_sn2.id = "snTimerYearList";
		_dv2.appendChild(_sn_prev);
		_dv2.appendChild(_sn1);
		_dv2.appendChild(_sn2);
		_dv2.appendChild(_sn_next);

		_dv.appendChild(_dv2);

		var _dv3 = document.createElement("div");
		_dv3.style.backgroundColor = "#ffffff";
		var _text = "";
		_text = '<table border="0" cellspacing="0" width="100%" class="time-list"><tr><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr>';
		_text += '</table>';
		_text += '<table border=0 cellspacing=0 cellpadding=0 width=100% height=120 class="date-list">';

		var n=0;
		for (var j=0;j<6;j++)
		{
			_text += ' <tr>';
			for (var i=0;i<7;i++)
			{
				_text += '<td id=timerItem'+n+' ></td>';
				n++;
			}
			_text += '</tr>';
		}
		_text += "<tr id='todayDate'><td colspan='7' class='day-msg'><a id='set-today' href='javascript:void(0);'>今天</a></td></tr>";
		_text += "</table>";
		_dv3.innerHTML = _text;

		_dv.appendChild(_dv3);

		var _dvMain = document.createElement("div");
		_dvMain.setAttribute("id","dvTimerContainer");
		_dvMain.style.cssText = "display:none; position:absolute;";
		document.body.appendChild(_dvMain);

		if(_isIE6)
		{
			var _ie = document.createElement("iframe");
			_ie.src = "https://www.tenpay.com/v2.0/inc/none.html";
			_ie.style.cssText = "width:178px;height:178px;position:absolute;top:0;z-index:-1;";
			_$("dvTimerContainer").appendChild(_ie);
		}

		_$("dvTimerContainer").appendChild(_dv);

		_$("dvTimerContainer").onmousedown = _clearEvent;
		_$("set-today").onclick = function()
		{
			_this.curControl.value = _getToday();
			_$("dvTimerContainer").style.display = "none";
			if(_this.obj.evn && typeof(_this.obj.evn[_this.curIndex]) == "function")
			{
				_this.obj.evn[_this.curIndex].call(null); //触发点击事件
			}
		};
		if(document.all)
		{
			document.attachEvent(
				"onmousedown",
				function()
				{
					if(_$("dvTimerContainer")) _$("dvTimerContainer").style.display = "none";
				}
			);
		}
		else
		{
			document.addEventListener("mousedown",function()
			{
				if(_$("dvTimerContainer")) _$("dvTimerContainer").style.display = "none";
			},
			false
			);
		}
	};
	var timeFormat = function(s)
	{
		var _r = /\d{8}/;
		if(_r.test(s)) return s;
		if(isNaN(s.replace(_re,""))) return false;
		var _l = s.split(_re);
		if(_l.length !== 3) return false;
		if(parseInt(_l[0],10) <= 0 || parseInt(_l[1],10) <= 0 || parseInt(_l[2],10) <= 0) return false;
		_l[1] = _l[1].length == 1 ? "0" + _l[1] : _l[1];
		_l[2] = _l[2].length == 1 ? "0" + _l[2] : _l[2];
		return (_l[0] + _l[1] + _l[2]);
	};
	if(document.all) window.attachEvent("onload",_init);
	else window.addEventListener("load",_init,false);
	return	{
		bind : _bindControl,
		format : timeFormat
	};
}
)();