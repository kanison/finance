var Browser=(function(){var userAgent=navigator.userAgent.toLowerCase();var env=null;return(env=userAgent.match(/msie ([\d.]+)/))?{type:"MSIE",version:env[1]}:(env=userAgent.match(/firefox\/([\d.]+)/))?{type:"FireFox",version:env[1]}:(env=userAgent.match(/opera.([\d.]+)/))?{type:"Opera",version:env[1]}:(env=userAgent.match(/version\/([\d.]+).*safari/))?{type:"Safari",version:env[1]}:(env=userAgent.match(/chrome\/([\d.]+)/))?{type:"Chrome",version:env[1]}:{type:"Unknown",version:0};})();if(window.Event&&"MSIE"!=Browser.type){function SearchEvent(){var func=SearchEvent.caller;while(func!=null){var arg=func.arguments[0];if(arg){if(String(arg.constructor).indexOf('Event')>-1){return arg;}}func=func.caller;}return null;}window.constructor.prototype.__defineGetter__("event",function(){return SearchEvent()});Event.prototype.__defineSetter__("returnValue",function(bool){if(!bool){this.preventDefault();}return bool;});Event.prototype.__defineSetter__("cancelBubble",function(bool){if(bool){this.stopPropagation();}return bool;});Event.prototype.__defineGetter__("clientX",function(){return this.pageX;});Event.prototype.__defineGetter__("clientY",function(){return this.pageY;});Event.prototype.__defineGetter__("keyCode",function(){return this.which;});Event.prototype.__defineGetter__("button",function(){return this.which;});Event.prototype.__defineGetter__("srcElement",function(){var n=this.target;while(n.nodeType!=1){n=n.parentNode;}return n;});Event.prototype.__defineGetter__("offsetX",function(){return this.layerX;});Event.prototype.__defineGetter__("offsetY",function(){return this.layerY;});Event.prototype.attachEvent=function(type,handler,isCapture){this.addEventListener(type.substring(2),handler,isCapture||false);};Event.prototype.detachEvent=function(type,handler,isCapture){this.removeEventListener(type.substring(2),handler,isCapture||false);};window.constructor.prototype.attachEvent=function(type,handler,isCapture){this.addEventListener(type.substring(2),handler,isCapture||false);};window.constructor.prototype.detachEvent=function(type,handler,isCapture){this.removeEventListener(type.substring(2),handler,isCapture||false);};document.constructor.prototype.attachEvent=function(type,handler,isCapture){this.addEventListener(type.substring(2),handler,isCapture||false);};document.constructor.prototype.detachEvent=function(type,handler,isCapture){this.removeEventListener(type.substring(2),handler,isCapture||false);};Element.prototype.attachEvent=function(type,handler,isCapture){this.addEventListener(type.substring(2),handler,isCapture||false);};Element.prototype.detachEvent=function(type,handler,isCapture){this.removeEventListener(type.substring(2),handler,isCapture||false);};HTMLElement.prototype.attachEvent=function(type,handler,isCapture){this.addEventListener(type.substring(2),handler,isCapture||false);};HTMLElement.prototype.detachEvent=function(type,handler,isCapture){this.removeEventListener(type.substring(2),handler,isCapture||false);};CSSStyleSheet.prototype.addRule=function(selector,rule,index){var _rule=selector+"{"+rule+"}";this.insertRule(_rule,index);};CSSStyleSheet.prototype.removeRule=function(index){this.deleteRule(index);};}
var TFL = TFL || {
  DOMAIN: "tenpay.com"
};

TFL.dom = TFL.dom || {
  $: function (id) {
    switch (typeof id) {
    case "object":
      return id;
      break;
    case "string":
      return document.getElementById(id);
      break;
    default:
      return null;
    }
  },
  _onload: [],
  loaded: function () {
    if (arguments.callee.DONE) return;
    arguments.callee.DONE = true;
    for(i = 0; TFL.dom._onload && i < TFL.dom._onload.length; i++) TFL.dom._onload[i]();
  },
  load: function (f) {
    this._onload.push(f);
    if (document.addEventListener) {
      document.addEventListener("DOMContentLoaded", this.loaded, null);
    }else if (/KHTML|WebKit/i.test(navigator.userAgent)) {
      var _timer = setInterval(function() {
        if (/loaded|complete/.test(document.readyState)) {
          clearInterval(_timer);
          delete _timer;
          this.loaded();
        }
      }, 10);
    }else if ("\v"=="v") {
      var proto = "src=\"javascript:void(0)\"";
      if (location.protocol == "https:") proto = "src=\"//0\"";
      document.write("<script id=\"js_ie_onload\" defer=\"defer\" " + proto + "><\/script>");
      var script = this.$("js_ie_onload");
      script.onreadystatechange = function() {
        if (this.readyState == "complete") TFL.dom.loaded();
      };
    }else {
      window.onload = this.loaded;
    }
  },
  getElementsByClassName : function (clsName,htmltag,root){
    var arr = new Array();
    var rootEl = arguments[2] || document;
    var elems = rootEl.getElementsByTagName(htmltag);
    for ( var i = 0; ( elem = elems[i] ); i++ ){
      if ( this.hasClass(clsName,elem)){
        arr[arr.length] = elem;
      }
    }
    return arr;
  },
  hasClass : function (clsName,elem) {
    var str = elem.className;
    var re = new RegExp('(?:^|\\s+)' + clsName + '(?:\\s+|$)') ;
    return re.test(str) ? 1 : 0 ;
  },
  addClass : function (clsName,elem) {
    if (!this.hasClass(clsName,elem)){elem['className'] = [elem['className'], clsName].join(' ');}
  },
  removeClass : function (clsName,elem) {
    var re = new RegExp('(?:^|\\s+)' + clsName + '(?:\\s+|$)', 'g');
    if (!this.hasClass(clsName,elem)) { return; }
    elem['className'] = elem['className'].replace(re, ' ');
    if ( this.hasClass(clsName,elem) ) {
       this.removeClass(clsName,elem);
    }
  },
  replaceClass : function (oldclsName,newclsName,elem) {
    var re = new RegExp('(?:^|\\s+)' + oldclsName + '(?:\\s+|$)', 'g');
    if ( !this.hasClass(oldclsName,elem)) {
        this.addClass(elem, newclsName);
        return; // note return
    }
    elem['className'] = elem['className'].replace(re, ' ' + newclsName + ' ');
    if ( this.hasClass(elem, oldclsName) ) {
        this.replaceClass(elem, oldclsName, newclsName);
    }
  }
};
//选择器,by jk
TFL.dom.selector = (function()
{
	var _$ = function(els)
	{
		this._els = [];
		var _R = /\<(\w+)\>/;
		this.isIE = document.all ? true : false;
		var _$$ = function(s)
		{
			return _R.test(s) ? document.createElement(s.replace(_R,"$1")) : document.getElementById(s);
		};
		for(var i = 0; i < els.length; i++)
		{
			var _el = typeof(els[i]) == "object" ? els[i] : _$$(els[i]);
			this._els.push(_el);
		}
		this.setArr(this._els);
	};

	_$.prototype =
	{
		setArr : function(arr)
		{
			this.length = 0;
			[].push.apply(this,arr);
		},
		each : function(evn,args)
		{
			for(var i = 0; i < this.length; i++)
			{
				var _args = args;
				var _isArr = false;
				var x = 0;
				for(; x < args.length; x++)
				{
					if(args[x].constructor == Array)
					{
						_isArr = true;
						break;
					}
				}
				if(_isArr)
				{
					var k = this.length > 1 ? i : 0;
					for(; k < args[x].length; k++)
					{
						_args = [];
						for(var j = 0; j < args.length; j++)
						{
							_args.push(args[j].constructor == Array ? args[j][k] : args[j]);
						}
						evn.apply(this[i],_args);
						if(this.length > 1) break;
					}
				}
				else evn.apply(this[i],_args);
			}
			return this;
		},
		css : function(n,v)
		{
			if(typeof(v) == "undefined") return this[0].style[n];
			return this.each(
					function(n,v)
					{
						this.style[n] = v;
					},[n,v]
				);
		},
		attr : function(n,v)
		{
			var fix =
			{
				"for": "htmlFor",
				"class": "className",
				"float": "cssFloat",
				innerHTML: "innerHTML",
				className: "className",
				value: "value",
				disabled: "disabled",
				checked: "checked"
			};
			if(typeof(v) == "undefined") return this[0][n];
			return this.each(
					function()
					{
						var _n = arguments[0];
						var _v = arguments[1];
						if(fix[_n]) this[fix[_n]] = _v;
						else
						{
							if(this.getAttribute != undefined) this.setAttribute(_n,_v);
							else this[_n] = arguments[_v];
						}
					},[n,v]
				);
		},
		html : function(s)
		{
			if(typeof(s) == "undefined") return this[0].innerHTML;
			return this.each(
					function()
					{
						this.innerHTML = arguments[0];
					},[s]
				);
		},
		val : function(v)
		{
			if(typeof(v) == "undefined") return this[0].value;
			return this.each(
					function()
					{
						this.value = arguments[0];
					},[v]
				);
		},
		append : function(o)
		{
			if(typeof(o) == "undefined") return false;
			return this.each(
				function()
				{
					this.appendChild(arguments[0]);
				},[o]
			);
		},
		remove : function()
		{
			for(var i = 0; i < this.length; i++)
			{
				if(this[i].removeNode) this[i].removeNode(true);
				else
				{
					if(this[i].parentNode)
					{
						this[i].parentNode.removeChild(this[i]);
					}
				}
				this[i] = null;
			}
			return null;
		},
		parent : function()
		{
			return this[0].parentNode;
		},
		bind : function(act,evn)
		{
			var _that = this;
			return this.each(
				function()
				{
					var evn = arguments[0];
					var fn = arguments[1];
					if(fn.constructor == Function)
					{
						if(!this.evns) this.evns = {};

						if(!this.evns[evn]) this.evns[evn] = [];

						var _is = false;

						for(var i = 0; i < this.evns[evn].length; i++)
						{
							if(this.evns[evn][i].toString() == fn.toString())
							{
								_is = true;
								break;
							}
						}
						if(!_is)
						{
							this.evns[evn].push(fn);
							_that.isIE ? this.attachEvent("on" + evn,fn) : this.addEventListener(evn,fn,false);
						}
					}
				},[act,evn]
			);
		},
		clear : function(act,evn)
		{
			var _that = this;
			return this.each(
				function()
				{
					if(this.evns)
					{
						var evn = arguments[0];
						var fn = arguments[1];

						if(this.evns[evn].length > 0)
						{
							for(var i = 0; i < this.evns[evn].length; i++)
							{
								if(this.evns[evn][i].toString() == fn.toString())
								{
									_that.isIE ? this.detachEvent("on" + evn,this.evns[evn][i]) : this.removeEventListener(evn,this.evns[evn][i],false);
								}
								for(var j = 0, n = 0; j < this.evns[evn].length; j++)
								{
									if(this.evns[evn][j] != this.evns[evn][i])
									{
										this.evns[evn][n++] = this.evns[evn][j];
									}
								}
								this.evns[evn].length -= 1;
								break;
							}
						}
					}
				},[act,evn]
			);
		}
	};
	return function(){return new _$(arguments)};
}
)();

TFL.cookie = {
  set: function (name, value, domain, path, hour) {
    if (hour) {
      var now = new Date();
      var expire = new Date();
      expire.setTime(parseFloat(now.getTime()) + 3600000 * hour);
    }
    document.cookie = name + "=" + value + "; " + (hour ? ("expires=" + expire.toUTCString() + "; ") : "") + (path ? ("path=" + path + "; ") : "path=/; ") + (domain ? ("domain=" + domain + ";") : ("domain=" + TFL.DOMAIN + ";"));
    return true;
  },
  get: function (name) {
    var re = new RegExp("(?:^|;+|\\s+)" + name + "=([^;]*)");
    var result = document.cookie.match(re);
    return (!result ? "" : result[1]);
  },
  del: function (name, domain, path) {
    document.cookie = name + "=; expires=Mon, 2 Mar 2009 19:00:00 UTC; " + (path ? ("path=" + path + "; ") : "path=/; ") + (domain ? ("domain=" + domain + ";") : ("domain=" + TFL.DOMAIN + ";"));
  }
};

TFL.validator = {
  isMobileNO: function (str) {
  	return /^1\d{10}$/.test(str);
  },
  isValidDate: function (year, month, day) {
    var y, m, d, now;
    now = new Date(year, month - 1, day);
    y = now.getFullYear();
    m = now.getMonth() + 1;
    d = now.getDate();
    return (year == y && month == m && day == d);
  },
  isCnID: function (strID) {
    var arrExp = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
    var arrValid = [1, 0, "X", 9, 8, 7, 6, 5, 4, 3, 2];//校验码
    var year, month, day;
    if (/^\d{15}$/.test(strID)) {
      year = "19" + strID.substr(6, 2);
      month = strID.substr(8, 2);
      day = strID.substr(10, 2);
      return this.isValidDate(year, month, day);
    }else if (/^\d{17}\d|x$/i.test(strID)) {
      var sum = 0, y;
      for (var i = 0; i < strID.length - 1; i++) sum += parseInt(strID.substr(i, 1), 10) * arrExp[i];
      y = sum % 11;
      year = strID.substr(6, 4);
      month = strID.substr(10, 2);
      day = strID.substr(12, 2);
      return (arrValid[y] == strID.substr(17, 1).toUpperCase() && this.isValidDate(year, month, day));
    }else return false;
  },
  isHkID: function (strID) {
  	var charCodeHashTable = {" ": 58, "A": 10, "B": 11, "C": 12, "D": 13, "E": 14, "F": 15, "G": 16, "H": 17, "I": 18, "J": 19, "K": 20, "L": 21, "M": 22, "N": 23, "O": 24, "P": 25, "Q": 26, "R": 27, "S": 28, "T": 29, "U": 30, "V": 31, "W": 32, "X": 33, "Y": 34, "Z": 35};
  	var sum, len, ch;
  	strID = strID.toUpperCase().replace(/\(|\)/g, "");
  	if (/^[ A-Z]{0,1}[A-Z]\d{6}[0-9A]$/.test(strID)) {
  	  var len = strID.length;
  	  sum = (len == 9)?0:(9 * 58);
  	  for (var i = len; i > 0; i--) {
  	    ch = strID.substr(len - i, 1);
  	    sum += ((ch in charCodeHashTable)?charCodeHashTable[ch]:parseInt(ch, 10)) * i;
  	  }
  	  return ((sum % 11) == 0)?true:false;
  	}else return false;
  }
};
/**
 * loader
 */
TFL.Loader = {
    loadScript : function(url, handler, args, isDestory){
        var head = document.getElementsByTagName("head")[0];
        var script = document.createElement("script");
        var id = "dynamic_script_" + ((new Date()).getTime());
        var eventType = (undefined !== script.onreadystatechange && undefined !== script.readyState) ? "onreadystatechange" : "onload";
        var _isDestory = typeof(isDestory) == "boolean" ? isDestory : true;
        script.type="text/javascript";
        script.src = url;
        script.id = id;
        script.attachEvent(eventType, function(){
            var state = script.readyState || "loaded";
            if("loaded" == state || "complete" == state){
                if(typeof(handler) == "string"){
                    setTimeout(function(){try{var hdl = eval(handler);hdl.apply(null, args);}catch(e){}}, 50);
                }else if(typeof(handler) == "function"){
                    setTimeout(function(){try{handler.apply(null, args);}catch(e){}}, 50);
                }
                if(_isDestory){
                    head.removeChild(script);
                }
                head = null;
                script = null;
            }
        });
        head.appendChild(script);
        return id;
    }
};
function Stat($domain, version, url){
    this.$domain = $domain || "www.tenpay.com";
    this.version = version || "";
    this.url = (location.protocol == "https:" ? "https://www.tenpay.com/zft/js/ping_tenpay.ziped.js" : "http://pingjs.qq.com/ping.js");
    this.loader = null;
    this.DOMAIN = {
        www : "www.tenpay.com",
        youhui : "youhui.tenpay.com",
        help : "help.tenpay.com",
        jump : "jump.tenpay.com",
        shouji : "shouji.tenpay.com",
        xinyongka : "xinyongka.tenpay.com",
        jiaofei : "jiaofei.tenpay.com",
        chong : "chong.tenpay.com",
        youxi : "youxi.tenpay.com",
        qbqd : "qbqd.tenpay.com",
        qq : "qq.tenpay.com",
        hongbao : "hongbao.tenpay.com",
        caipiao : "500wan.zone.tenpay.com",
        z : "z.tenpay.com",
        csair : "qq.csair.com",
        hnair : "hnair.qq.com",
        ceair : "ceair.qq.com",
        air : "air.qq.com",
        airb2c : "air.tenpay.com",
        wallet : "wallet.tenpay.com",
        mch : "mch.tenpay.com",
        fund : "fund.tenpay.com",
        action : "action.tenpay.com",
        jipiao : "jipiao.tenpay.com",
        jiudian : "jiudian.tenpay.com",
        gwt : "gwt.tenpay.com"
    };
};
Stat.prototype = {
    resetGlobalValue : function(){
        pvCurDomain="";
        pvCurUrl="";
        pvCurParam="";
        pvRefDomain="";
        pvRefUrl="";
        pvRealDomain="";
        pvRefParam="";
        pvRepeatCount = 1;
    },
    main : function(path){
        if(typeof(pgvMain) == "function"){
            this.resetGlobalValue();
            pgvMain("", {
                virtualDomain:this.$domain,
                virtualURL:path
            });
        }
    },
    clickStat : function(tag, _domain){
        var s = tag.replace(/^([^\.]+)/g, "$1"+("" !== this.version ? "_" + this.version : this.version));
        s = this.parseADTAG(s);
        if(typeof(pgvSendClick) == "function"){
            pvCurDomain = _domain ? this.DOMAIN[_domain] || this.$domain : this.$domain;
            pgvSendClick({hottag:s});
        }else{
            var _this = this;
            var id = "";
            id = TFL.Loader.loadScript(this.url, function(sTag){
                _this.loader = document.getElementById(id);
                if(typeof(pgvSendClick) == "function"){
                    pvCurDomain = _this.$domain;
                    pgvSendClick({hottag:sTag});
                }
            }, [s], false);
        }
    },
    start : function(path){
        if(typeof(pgvMain) == "function"){
            this.resetGlobalValue();
            pgvMain("pathtrace", {virtualURL:path, virtualDomain:this.$domain, pathStart:true, tagParamName:"ADTAG", useRefUrl:true});
        }
    },
    end : function(){
        if(typeof(pgvMain) == "function"){
            this.resetGlobalValue();
        	pgvMain("pathtrace", {endPath:true});
        }
    },
    keyPath : function(path, keyTag, nIndex){
        if(typeof(pgvMain) == "function"){
            this.resetGlobalValue();
            pgvMain("pathtrace", {virtualURL:path, virtualDomain:this.$domain, pathStart:true, tagParamName:"ADTAG", useRefUrl:true, keyPathTag:keyTag, nodeIndex:nIndex});
        }
    },
    parseADTAG : function(tag){
         var type = (Browser.type).toUpperCase();
        if("FIREFOX" == type){
            tag =  tag.replace(/^([^\.]+)/g, "$1_"+type);
        }
        return tag;
    },
    parsePath : function(path){
        var type = (Browser.type).toUpperCase();
        if("FIREFOX" == type){
            var aPath = path.split(".");
            var len = aPath.length;
            if(len > 1){
                aPath[len-2] = aPath[len-2] + "_" + type;
                path = aPath.join(".");
            }else{
                path = path + "_" + type;
            }
        }
        return path;
    },
    load : function(path, param){
        var sPath = (typeof(path) == "string" && "" != path) ? path : location.pathname;
        var oTrace = param || null;
        if(null == this.loader){
            var _this = this;
            var id = "";
            id = TFL.Loader.loadScript(this.url, function(path, trace){
                _this.loader = document.getElementById(id);
                _this.innerCall(path, trace);
            }, [sPath, oTrace], false);
        }else{
            this.innerCall(sPath, oTrace);
        }
    },
    innerCall : function(path, trace){
        path = this.parsePath(path);
        if(null != trace){
            if(true === trace.start){
                this.start(path);
            }else if(false === trace.end){
                this.end();
            }else if(true === trace.key){
                this.keyPath(path, trace.node, (trace.index||1));
            }else{
                this.main(path);
            }
        }else{
            this.main(path);
        }
    },
    multiLoad : function(items){
        var size = items.length;
        var count = 0;
        var _this = this;
        if(null == this.loader){
            var _this = this;
            var id = "";
            id = TFL.Loader.loadScript(this.url, function(list){
                _this.loader = document.getElementById(id);
                var intv = setInterval(function(){
                    var size = list.length;
                    if(count >= size){
                        clearInterval(intv);
                        return true;
                    }
                    var o = list[count];
                    _this.pgv(o.domain||"www.tenpay.com", o.path||location.pathname, o.trace||null);
                    count++;
                }, 50);
            }, [items], false);
        }else{
            var intv = setInterval(function(){
                if(count >= size){
                    clearInterval(intv);
                    return true;
                }
                var o = items[count];
                _this.pgv(o.domain||"www.tenpay.com", o.path||location.pathname, o.trace||null);
                count++;
            }, 50);
        }
    },
    pgv : function(_domain, path, param){
        this.$domain = this.DOMAIN[_domain] || "www.tenpay.com";
        path = path || location.pathname;
        param = param || null;
        this.load(path, param);
    }
};
TFL.Ping = new Stat();
TFL.Life = new Stat("life.tenpay.com","","");
TFL.YOUHUI = new Stat("youhui.tenpay.com", "","");
TFL.Portal = new Stat("portal.tenpay.com", "", "");
TFL.Stat = new Stat();
TFL.virtualAjax = (function () {
  var f, ifr;
  var genFormElement = function (para) {
    para = para.replace(/^\?/g, "");
    var tmpArr = para.split("&"), len = tmpArr.length, result = [];
    for (var i = 0; i < len; i++)  result.push(tmpArr[i].replace(/^([^=]+)=([^=]+)$/, '<input type="hidden" name="$1" value="$2" />'));
    return result.join("");
  };
  var createForm = function (obj) {
    var form = TFL.dom.$("ajax_form");
    if (!form) {
      form = document.createElement("form");
      form.id = "ajax_form";
      form.style.display = "none";
      document.body.appendChild(form);
    }
    form.action = obj.url;
    form.method = obj.method || "get";
    form.target = "ajax_hide_win";
    form.innerHTML = genFormElement(obj.vars);
    f = form;
  };
  var createIFrame = function (obj) {
    var iframe = TFL.dom.$("ajax_hide_win");
    if (!iframe) {
      iframe = ("\v" == "v") ? document.createElement('<iframe name="ajax_hide_win">') : document.createElement("iframe");
      iframe.id = "ajax_hide_win";
      iframe.name = "ajax_hide_win";
      iframe.style.display = "none";
      iframe.src = "https://www.tenpay.com/v2.0/none.html";
      document.body.appendChild(iframe);
    }
    var handler = function () {
      var para = "";
      if (obj.getType == "2") {
        para = iframe.contentWindow.document.XMLDocument || iframe.contentWindow.document;//responseXML
        if (!para.async) para = '<?xml version="1.0" encoding="gb2312"?><root><retcode>-1</retcode><retmsg>数据返回异常，请您稍后重新尝试！</retmsg></root>';
      }else
        para = iframe.contentWindow.document.body.innerHTML;//responseText
      if (!!obj.callBack) {
        obj.callBack(para);
        obj.callBack = null;
      }
    };
    iframe.attachEvent("onload", handler);
    ifr = iframe;
  };
  var send = function (obj) {
    obj = {
      url: obj.url,
      vars: obj.vars || "",
      method: obj.method || "get",
      callBack: obj.callBack || function () {},
      getType: obj.getType || "2"
    };
    createForm(obj);
    createIFrame(obj);
    f.submit();
  };
  var byForm = function (form, callBack, getType) {
    var result = [], len = form.elements.length, el, obj;
    for(var i = 0; i < len; i++) {
      el = form.elements[i];
      if (el.type && /^CHECKBOX|RADIO$/.test(el.type.toUpperCase()) == "CHECKBOX" && !form.elements[i].checked) continue;
      if (el.name && el.value) reqdat.push(el.name + "=" + (el.value).replace(/%/g,"%25").replace(/\r\n/g,"%0D%0A").replace(/=/g,"%3D").replace(/&/g,"%26").replace(/\?/g,"%3F").replace(/#/g,"%23").replace(/\+/g,"%2B"));
    }
    obj = {
      url: form.action,
      vars: result.join("&") || "",
      method: form.method || "get",
      callBack: callBack || function () {},
      getType: getType || "2"
    };
    send(obj);
  };

  return {
    send: send,
    byForm: byForm
  }
})();
TFL.ajax2 = function()
{
	this._ajaxVars = null;
	this._preFix = null;
	this._request = null;
	this._isSend = false;
	this.obj = null;
};
TFL.ajax2.prototype =
{
	ajaxRequest : function(obj)
	{
		if(this._isSend) return false;
		this._request = TFL.ajax2.initRequest();
		this.obj = obj;
		this._ajaxVars =
		{
			url : this.obj.url,
			method : this.obj.method || "get",
			callBack : this.obj.callBack || function(){},
			vars : this.obj.vars || "",
			getType : this.obj.getType || 2
		};
		(this._ajaxVars.method == "post") ? this.post() : this.get();
	},
	get : function()
	{
		this._isSend = true;
		var _ajaxVars = this._ajaxVars,_request = this._request,ajaxCallBack = this.ajaxCallBack,that = this;
		var ptm = new Date().getTime();
		_ajaxVars.vars += (_ajaxVars.vars == "" ? "ptm="+ptm.toString() : "&ptm=" + ptm.toString());
		_request.open("GET",_ajaxVars.url + "?" + _ajaxVars.vars,true);
		_request.onreadystatechange = function()
		{
			if(_request.readyState !== 4) {this._isSend = false;return};
			if(_request.status === 200)
			{
				if(_request.responseXML === undefined)
				{
					ajaxCallBack(_request.responseText,new DOMParser().parseFromString(_request.responseText, "text/xml"),that);
				}
				else
				{
					ajaxCallBack(_request.responseText,_request.responseXML,that);
				}
			}
			else
			{
				this._isSend = false;
				var retcode = _request.status;
				var retmsg = "系统故障：访问的页面出现异常！";
				var objXml = "<?xml version=\"1.0\" encoding=\"gb2312\"?><root><retcode>" + retcode + "</retcode><retmsg>" + "[" + retcode + "]" + retmsg + "</retmsg></root>";
				_ajaxVars.getType = 2;
				ajaxCallBack("", objXml,that);
			}
		};
		_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GB2312");
		_request.setRequestHeader("Cache-Control","no-cache");
		_request.send("");
	},
	post : function()
	{
		this._isSend = true;
		var _ajaxVars = this._ajaxVars,_request = this._request,ajaxCallBack = this.ajaxCallBack,that = this;
		_request.open("POST",_ajaxVars.url,true);
		_request.onreadystatechange = function()
		{
			if(_request.readyState !== 4) {this._isSend = false;return};
			if(_request.status === 200)
			{
				if(_request.responseXML === undefined)
				{
					ajaxCallBack(_request.responseText,new DOMParser().parseFromString(_request.responseText, "text/xml"),that);
				}
				else
				{
					ajaxCallBack(_request.responseText,_request.responseXML,that);
				}
			}
			else
			{
				var retcode = _request.status, retmsg = "系统故障：访问的页面出现异常！", objXml;
				switch (retcode) {
				  //case 404:
				  case 12029:
				  case 12030:
				  case 12031:
				  case 12152:
				  case 12159:
					this._isSend = false;
					setTimeout(function () {TFL.Ping.clickStat("TENPAY.BASE.AJAX_ERROR." + retcode);}, 0);
					TFL.virtualAjax.send(_ajaxVars);
					break;
				  default:
					objXml = "<?xml version=\"1.0\" encoding=\"gb2312\"?><root><retcode>" + retcode + "</retcode><retmsg>" + "[" + retcode + "]" + retmsg + "</retmsg></root>";
					_ajaxVars.getType = 2;
					ajaxCallBack("", objXml,that);
				}
			}
		};
		_request.setRequestHeader("Content-Length",_ajaxVars.vars.length);
		_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		_request.send(_ajaxVars.vars);
	},
	ajaxByForm : function(form,callBack)
	{
		var reqdat = "";
		for(var i=0; i < form.length; i++)
		{
			if(form.elements[i].type && form.elements[i].type.toUpperCase() == "CHECKBOX" && !form.elements[i].checked)
				continue;

			if(form.elements[i].type && form.elements[i].type.toUpperCase() == "RADIO" && !form.elements[i].checked)
				continue;

			if(form.elements[i].name && form.elements[i].value && form.elements[i].name != "" && form.elements[i].value != "")
			{
				reqdat = reqdat + "&" + form.elements[i].name + "=" + (form.elements[i].value).replace(/%/g,"%25").replace(/\r\n/g,"%0D%0A").replace(/=/g,"%3D").replace(/&/g,"%26").replace(/\?/g,"%3F").replace(/#/g,"%23").replace(/\+/g,"%2B");
			}
		}
		this.ajaxRequest
		(
			{
				url : form.action,
				vars : reqdat.substr(1),
				callBack : callBack
			}
		);
	},
	ajaxCallBack : function(t,x,that)
	{
		that._isSend = false;
		that._request = null;
		(that._ajaxVars.getType && that._ajaxVars.getType == 1) ?
			that._ajaxVars.callBack(t) :
			that._ajaxVars.callBack(x);
	}
};
TFL.ajax2.initRequest = function()
{
	var init_preFix = function()
	{
		var _preFixes = ["MSXML3", "MSXML2", "MSXML", "Microsoft"];
		var obj;
		for(var i = 0; i < _preFixes.length; i++)
		{
			try
			{
				obj = new ActiveXObject(_preFixes[i] + ".XmlHttp");
				return _preFix = _preFixes[i];
			}
			catch (ex)
			{};
		}
		throw new Error("您没有安装XML解析器,请使用INTERNET EXPLORE 5以上的浏览器.");
	};
	try
	{
		if (window.XMLHttpRequest)
		{
			return new XMLHttpRequest();
		}
		if (window.ActiveXObject)
		{
			return new ActiveXObject(init_preFix() + ".XmlHttp");
		}
	}
	catch (ex)
	{
		alert(ex.message);
	};

	return false;
};
/*TFL.ajax = (function()
{
	var _send = function(obj)
	{
		var _ajax = new TFL.ajax2();
		_ajax.ajaxRequest(obj);
	};
	var _byForm = function(form,callBack)
	{
		var _ajax = new TFL.ajax2();
		_ajax.ajaxByForm(form,callBack);
	};
	return {send : _send,byForm : _byForm};
})();*/
TFL.ajax =
(
	function()
	{
		var _ajaxVars = null;
		var _preFix = null;
		var _request = null;
		var _isSend = false;
		var ajaxRequest = function(obj)
		{
			if(_isSend) return false;
			_request = initRequest();
			_ajaxVars =
			{
					url : obj.url,
					method : obj.method || "get",
					callBack : obj.callBack || function(){},
					vars : obj.vars || "",
					getType : obj.getType || 2
			};
			(_ajaxVars.method == "post") ? post() : get();
		};

		var initRequest = function()
		{
			var init_preFix = function()
			{
				if(_preFix)
				{
					return _preFix;
				}
				var _preFixes = ["MSXML3", "MSXML2", "MSXML", "Microsoft"];
				var obj;
				for(var i = 0; i < _preFixes.length; i++)
				{
					try
					{
						obj = new ActiveXObject(_preFixes[i] + ".XmlHttp");
						return _preFix = _preFixes[i];
					}
					catch (ex)
					{};
				}
				throw new Error("您没有安装XML解析器,请使用INTERNET EXPLORE 5以上的浏览器.");
			};
			try
			{
				if (window.XMLHttpRequest)
				{
					return new XMLHttpRequest();
				}
				if (window.ActiveXObject)
				{
					return new ActiveXObject(init_preFix() + ".XmlHttp");
				}
			}
			catch (ex)
			{
				alert(ex.message);
			};

			return false;
		};

		var get = function()
		{
			if(_isSend) return false;
			_isSend = true;
			var now = new Date();
			var rnd = Math.floor(Math.random() * 100000);
			var ptm = now.getSeconds().toString()+rnd.toString()+now.getMinutes().toString();
			_ajaxVars.vars += (_ajaxVars.vars == "" ? "ptm="+ptm.toString() : "&ptm=" + ptm.toString());
			_request.open("GET",_ajaxVars.url + "?" + _ajaxVars.vars,true);
			_request.onreadystatechange = function()
			{
				if(_request.readyState !== 4) {_isSend = false;return};
				if(_request.status === 200)
				{
					if(_request.responseXML === undefined)
					{
						ajaxCallBack(_request.responseText,new DOMParser().parseFromString(_request.responseText, "text/xml"));
					}
					else
					{
						ajaxCallBack(_request.responseText,_request.responseXML);
					}
				}
				else
				{
					_isSend = false;
					var retcode = _request.status;
					var retmsg = "系统故障：访问的页面出现异常！";
					var objXml = "<?xml version=\"1.0\" encoding=\"gb2312\"?><root><retcode>" + retcode + "</retcode><retmsg>" + "[" + retcode + "]" + retmsg + "</retmsg></root>";
					_ajaxVars.getType = 2;
					ajaxCallBack("", objXml);
				}
			};
			_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GB2312");
			_request.setRequestHeader("Cache-Control","no-cache");
			_request.send("");
		};

		var post = function()
		{
			if(_isSend) return false;
			_isSend = true;
			_request.open("POST",_ajaxVars.url,true);
			_request.onreadystatechange = function()
			{
				if(_request.readyState !== 4) {_isSend = false;return};
				if(_request.status === 200)
				{
					if(_request.responseXML === undefined)
					{
						ajaxCallBack(_request.responseText,new DOMParser().parseFromString(_request.responseText, "text/xml"));
					}
					else
					{
						ajaxCallBack(_request.responseText,_request.responseXML);
					}
				}
				else
				{
					var retcode = _request.status, retmsg = "系统故障：访问的页面出现异常！", objXml;
					switch (retcode) {
					  //case 404:
					  case 12029:
					  case 12030:
					  case 12031:
					  case 12152:
					  case 12159:
						_isSend = false;
						setTimeout(function () {TFL.Ping.clickStat("TENPAY.BASE.AJAX_ERROR." + retcode);}, 0);
						TFL.virtualAjax.send(_ajaxVars);
						break;
					  default:
						objXml = "<?xml version=\"1.0\" encoding=\"gb2312\"?><root><retcode>" + retcode + "</retcode><retmsg>" + "[" + retcode + "]" + retmsg + "</retmsg></root>";
						_ajaxVars.getType = 2;
						ajaxCallBack("", objXml);
					}
				}
			};
			_request.setRequestHeader("Content-Length",_ajaxVars.vars.length);
			_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			_request.send(_ajaxVars.vars);
		};

		var ajaxByForm = function(form,callBack)
		{
			var reqdat = "";
			for(var i=0; i < form.length; i++)
			{
				if(form.elements[i].type && form.elements[i].type.toUpperCase() == "CHECKBOX" && !form.elements[i].checked)
					continue;

				if(form.elements[i].type && form.elements[i].type.toUpperCase() == "RADIO" && !form.elements[i].checked)
					continue;

				if(form.elements[i].name && form.elements[i].value && form.elements[i].name != "" && form.elements[i].value != "")
				{
					reqdat = reqdat + "&" + form.elements[i].name + "=" + (form.elements[i].value).replace(/%/g,"%25").replace(/\r\n/g,"%0D%0A").replace(/=/g,"%3D").replace(/&/g,"%26").replace(/\?/g,"%3F").replace(/#/g,"%23").replace(/\+/g,"%2B");
				}
			}
			ajaxRequest
			(
				{
					url : form.action,
					vars : reqdat.substr(1),
					callBack : callBack
				}
			);
		};

		var ajaxCallBack = function(t,x)
		{
			_isSend = false;
			_request = null;
			(_ajaxVars.getType && _ajaxVars.getType == 1) ?
				_ajaxVars.callBack(t) :
				_ajaxVars.callBack(x);
		};

		return {
			send : ajaxRequest,
			byForm : ajaxByForm
		};
	}
)
();

TFL.xml = function(x)
{
	if(typeof(x) == "string")
	{
		if("undefined" != typeof(DOMParser))
	   	{
            var parser = new DOMParser();
            this.obj = parser.parseFromString(x, "application/xml");
	   	}
	    else if(window.ActiveXObject)
	    {
	        this.obj = new ActiveXObject("Microsoft.XMLDOM");
	        this.obj.async = false;
            this.obj.loadXML(x);
	        //while(obj.readyState != 4) {};//
	    }
        else
        {
            var url = "data:text/xml;charset=utf-8," + encodeURIComponent(x);
            var request = new XMLHttpRequest( );
            request.open("GET", url, false);
            request.send(null);
            this.obj = request.responseXML;
            request = null;
        }
	}
	else if(typeof(x) == "object")
	{
		this.obj = x;
	}
	else
	{
		//throw new Error("不能识别的对象");
	}
};

TFL.xml.prototype = {
	/**
	 * 取得单个节点值
	 */
	getVal : function(n)
	{
		var _rtn = "";
		try{_rtn = this.obj.getElementsByTagName(n)[0].firstChild.data;}catch(s){_rtn = ""};
		return _rtn;
	},
	/**
	 * 取得一组节点
	 */
	getNodes : function(n)
	{
		var _rtn = "";
		try{_rtn = this.obj.getElementsByTagName(n);}catch(s){_rtn = ""};
		return _rtn;
	},
	/**
	 * 取得一组子节点
	 */
	getChildNodes : function(p,n)
	{
		var _rtn = "";
		try{_rtn = p.getElementsByTagName(n);}catch(s){_rtn = ""};
		return _rtn;
	},
	/**
	 * 取得父级节点
	 */
	getParentNode : function(n)
	{
		var _rtn = "";
		try{_rtn = n.parentNode;}catch(s){_rtn = ""};
		return _rtn;
	},
	/**
	 * 取得属性符合的节点
	 */
	getAttVal : function(n,attN)
	{
		var _rtn = "";
		try
		{
			for(var i=0; i<n.attributes.length; i++)
			{
				if(n.attributes[i].nodeName == attN)
				{
					_rtn = n.attributes[i].nodeValue;
					break;
				}
			}
		}
		catch(ex)
		{
			_rtn = "";
		}

		return _rtn;
	},
	/**
	 * 取得当前节点内容
	 */
	getCurNodeVal : function(n)
	{
		var _rtn = "";
		try{_rtn = n.firstChild.data;}catch(s){_rtn = ""};
		return _rtn;
	}
};


/*******************************************************************************************************************************************************
* 财付通密码输入相关处理函数:使用公司QQEDIT控件
*******************************************************************************************************************************************************/
function CQQEdit()
{
//是否使用QQEDIT控件
this.m_bUseFlag = true;
//QQEDIT控件版本
//控件GUID
//this.m_sClassid = "clsid:D151948A-F3F3-41FE-BE76-07C64ACE8B53";//"clsid:E787FD25-8D7C-4693-AE67-9406BC6E22DF";
this.m_sVersion = "1,1,0,4";
//控件GUID
this.m_sClassid = "clsid:E787FD25-8D7C-4693-AE67-9406BC6E22DF";
//控件路径
this.m_sLocate = "https://www.tenpay.com/download/qqcert.cab";
this.CheckValid = CQQEdit_CheckValid;
this.Reload = CQQEdit_Reload;
this.GetStrongPasswd = CQQEdit_GetStrongPasswd;
this.GetRsaPassword = CQQEdit_GetRsaPasswd;
this.GetRsaPassword2 = CQQEdit_GetRsaPasswd2;
this.GetShaPassword = CQQEdit_GetSha1Passwd;
this.GetTmFrmVcd = CQQEdit_GetTmFrmVcd;
this.GetTmFrmNumStr= CQQEdit_GetTmFrmNumStr;
//绘出控件
this.Draw = CQQEdit_Draw;
}
function CQQEdit_GetTmFrmNumStr(str)
{
var res="";
for(i=0;i<str.length;i++)
{
res += "3";
res += str.substring(i,i+1);
}
return res;
}
function CQQEdit_GetTmFrmVcd(vcd)
{
var tm = "12345678";
if(vcd.length != 4)
{
tm += "901234567890";
}
else
{
for(i=0 ;i <4;i++)
{
var tmp = vcd.charCodeAt(i).toString();
if(tmp.length != 3)
tm += "0" ;
tm += tmp;
}
}
return tm;
}
function CQQEdit_Draw(idname, tabindex, other)
{
var strObj;
strObj = "<object id=\"" + idname + "\" ";
strObj+= "classid=\"" + this.m_sClassid + "\" ";
strObj+= " codebase=\"" + this.m_sLocate + "#Version=" + this.m_sVersion + "\" viewastext";
if(tabindex)
strObj+=" tabindex=\"" + tabindex + "\"";
if(other)
strObj+=" " + other;
strObj+= "></object>";
return strObj;
try{
if(document.getElementById(idname).Mode==null){
document.write("<img src=\"/zft/images/logind.gif\"><a href=\"/download/qqcert.exe\"><font style=\"font-size:12px\" color=ff6600>无法输入登录密码？");
document.write("请点此安装安全控件后刷新");
}
} catch(er) {}
}
function CQQEdit_GetSha1Passwd(ctl,seed)
{
ctl.SetSalt(seed);
shap = ctl.GetSha1Value();
return  shap;
}
function CQQEdit_GetRsaPasswd(ctl,seed)
{
ctl.SetSalt(seed);
rsap = ctl.GetRsaPassword();
return rsap;
}
function CQQEdit_GetRsaPasswd2(ctl,seed)
{
ctl.SetSalt(seed);
rsap = ctl.GetRsaPassword2();
return rsap;
}
//沿用历史
function CQQEdit_GetStrongPasswd(ctl,seed)
{
ctl.SetSalt(seed);
shap = ctl.GetSha1Value();
rsap = ctl.GetRsaPassword();
return (seed + shap + rsap);
}
function CQQEdit_CheckValid(ctl)
{
try//因为高版本控件不支持GetPasswordInfo
{
var inf = ctl.GetPasswordInfo();
if(inf & 1)
{
alert("安全校验码失效，请刷新页面。");
this.Reload();
return false;
}
return true;
}
catch(exp)
{
return true;
}
}
function CQQEdit_Reload()
{
window.location.reload();
}
TFL.qqedit = new CQQEdit();

TFL.amount =
{
	format : function(val)
	{
		if(typeof val != "string") val = val.toString();
		if(val == "") return false;
		if((/[^\d\.\,]/g).test(val)) return false;
		val = val.replace(/\,/,"");
		if(val.split(".").length > 2 || parseFloat(val) > 999999999999.99) return false;
		var _len = val.length;
		for(var i = 0;i < _len;i++)
		{
			if(val.substr(0,1) == "0" && val != "0") val = val.substr(1);
			if(val.indexOf(".") > -1 && val.split(".")[1].length > 2 && val.substr(val.length - 1,1) == "0") val = val.substr(0,val.length - 1);
		}
		if(val.indexOf(".") > -1)
		{
			if(val.split(".")[1].length > 2) return false;
			if(val.split(".")[0] == "") val = "0" + val;
			if(val.split(".")[1].length == 1) val = val + "0";
			if(val.split(".")[1] == "") val = val + "00";
		}
		else
		{
			val = val + ".00";
		}
		if(parseFloat(val.replace(".","")) == 0) return "0";
		return val;
	},
	split : function(val)
	{
		if(!this.format(val)) return false;
		val = this.format(val);
		var _f = _l = "";
		if(val.indexOf(".") > -1)
		{
			_f = val.split(".")[0];
			_l = val.split(".")[1];
		}
		else _f = val;
		if(_f.length > 3)
		{
			var _c = (_f.length % 3) > 0 ? parseInt(_f.length / 3) + 1 : parseInt(_f.length / 3);
			var _result = "";
			for(var i = 0;i < _c;i++)
			{
				if(i == 0 && _f.length % 3 > 0){_result += _f.substr(0,_f.length % 3) + ",";_f = _f.substr(_f.length % 3);}
				else{_result += _f.substr(0,3) + ",";_f = _f.substr(3);}
			}
			_f = _result.substr(0,_result.length - 1);
		}
		val = (val.indexOf(".") > -1 ? (_f + "." + _l) : _f);
		return val;
	},
	cn : function(val)
	{
		if(!this.format(val)) return false;
		val = this.format(val);
		if(Number(val) == 0) return "零";
		var _ZERO = "零",_ONE = "壹",_TWO = "贰",_THREE = "叁",_FOUR = "肆",_FIVE = "伍",_SIX = "陆",_SEVEN = "柒",_EIGHT = "捌",_NINE = "玖",_TEN = "拾",_HUNDRED = "佰",_THOUSAND = "仟",_TEN_THOUSAND = "万",_HUNDRED_MILLION = "亿",_SYMBOL="",_DOLLAR = "元",_TEN_CENT = "角",_CENT = "分",_INTEGER = "整";
		var integral;
		var decimal;
		var _result;
		var parts;
		var digits, radices, bigRadices, decimals;
		var zeroCount;
		var i, p, d;
		var quotient, modulus;

		parts = val.split(".");
		if (parts.length > 1)
		{
			integral = parts[0];
			decimal = parts[1];
			decimal = decimal.substr(0, 2);
		}
		else
		{
			integral = parts[0];
			decimal = "";
		}
		digits = new Array(_ZERO, _ONE, _TWO, _THREE, _FOUR, _FIVE, _SIX, _SEVEN, _EIGHT, _NINE);
		radices = new Array("", _TEN, _HUNDRED, _THOUSAND);
		bigRadices = new Array("", _TEN_THOUSAND, _HUNDRED_MILLION);
		decimals = new Array(_TEN_CENT, _CENT);
		_result = "";
		if (Number(integral) > 0)
		{
			zeroCount = 0;
			for (i = 0; i < integral.length; i++)
			{
				p = integral.length - i - 1;
				d = integral.substr(i, 1);
				quotient = p / 4;
				modulus = p % 4;
				if (d == "0")
					zeroCount++;
				else
				{
					if (zeroCount > 0)
						_result += digits[0];
					zeroCount = 0;
					_result += digits[Number(d)] + radices[modulus];
				}
				if (modulus == 0 && zeroCount < 4)
					_result += bigRadices[quotient];
			}
			_result += _DOLLAR;
		}
		if (decimal != "")
		{
			for (i = 0; i < decimal.length; i++)
			{
				d = decimal.substr(i, 1);
				if (d != "0")
					_result += digits[Number(d)] + decimals[i];
			}
		}
		if (_result == "")
			_result = _ZERO + _DOLLAR;
		if (Number(decimal) == 0)
			_result += _INTEGER;
		_result = _SYMBOL + _result;
		return _result;
	},
	fen2Yuan : function(val)
	{
		var result, re = /^[\+|-]?[0-9]+$/;
		if (typeof(val) != "string") val = val.toString();
		return (re.test(val)) ? (parseFloat(val) / 100).toFixed(2) : "0.00";
	},
	yuan2Fen : function(val)
	{
		var result, re = /^[\+|-]?\d+(\.\d+)?$/;
		if (typeof(val) != "string") val = val.toString();
		return (re.test(val)) ? (parseFloat(val) * 100).toFixed(0) : "0";
	},
	getBalanceCallBack : null,
	getBalance : function()
	{
		TFL.ajax.send({vars : "",url : "/cgi-bin/v1.0/cft_balance.cgi",callBack : this.getBalanceResult});
	},
	getBalanceResult : function(x)
	{
		var _x = new TFL.xml(x);
		if(TFL.amount.getBalanceCallBack) TFL.amount.getBalanceCallBack.call(this,_x.getVal("balance_available"));
	}
};

TFL.Tween = {
  Sine: {
    easeOut: function(t, b, c, d) {
      return c * Math.sin(t / d * (Math.PI / 2)) + b;
    }
  }
};

TFL.Ease = {
  Height: {
    obj: null,
    callBack: null,
    direction: "forward",
    flag: true,
    /**
     *  t: current time 当前时间
     b: beginning value 初始值
     c: change in value 变化量
     d: duration 持续时间
     a: 偏移量
     */
    paras: {t: 0, b: 0, c: 0, d: 100, a: 1},
    start: function() {
      this.flag = false;
      var o = typeof this.obj == "object" ? this.obj :TFL.dom.$(this.obj);
      if (this.direction == "back") {
        o.style.height = this.paras.c + this.paras.b * 2 - Math.ceil(TFL.Tween.Sine.easeOut(this.paras.t, this.paras.b, this.paras.c, this.paras.d)) + "px";
      }else {
        o.style.height = Math.ceil(TFL.Tween.Sine.easeOut(this.paras.t, this.paras.b, this.paras.c, this.paras.d)) + "px";
      }
      if (this.paras.t < this.paras.d) {
        this.paras.t += this.paras.a;
        setTimeout("TFL.Ease.Height.start()", 10);
      }else {
        this.flag = true;
        (this.callBack != null) ? this.callBack.call(this) : "";
      }
    }
  },
  Alpha: {
    flag: true,
    obj: null,
    max: 0,
    min: 0,
    callBack: null,
    show: function() {
      this.flag = false;
      if (this.min >= this.max) {
        this.callBack != null ? this.callBack.call(this) : "";
        this.flag = true;
        return false;
      }
      var o = (typeof this.obj == "object") ? this.obj :TFL.dom.$(this.obj);
      this.min += 10;
      o.style.filter = "Alpha(Opacity=" + this.min + ")";
      o.style.opacity = this.min / 100;
      setTimeout("TFL.Ease.Alpha.show.call(TFL.Ease.Alpha)", 20);
    },
    hide: function() {
      this.flag = false;
      if (this.max <= this.min) {
        this.callBack != null ? this.callBack.call(this) : "";
        this.flag = true;
        return false;
      }
      var o = (typeof this.obj == "object") ? this.obj : TFL.dom.$(this.obj);
      this.max -= 10;
      o.style.filter = "Alpha(Opacity=" + this.max + ")";
      o.style.opacity = this.max / 100;
      setTimeout("TFL.Ease.Alpha.hide.call(TFL.Ease.Alpha)", 20);
    }
  }
};

/*************************************************
 * md5相关处理函数
 *************************************************/
var hexcase = 1;
var b64pad = "";
var chrsz = 8;
var mode = 32;

function preprocess(form)
{
  var str = "";
  str += form.verifycode.value;
  str = str.toUpperCase();
  form.p.value = md5(md5_3(form.p.value)+str);
  return true;
}

function md5_3(s)
{
  var tmp = new Array;
  tmp = core_md5(str2binl(s), s.length * chrsz);
  tmp = core_md5(tmp, 16 * chrsz);
  tmp = core_md5(tmp, 16 * chrsz);
  return binl2hex(tmp);
}

function md5(s)
{
  return hex_md5(s);
}
function hex_md5(s)
{
  return binl2hex(core_md5(str2binl(s), s.length * chrsz));
}

function b64_md5(s)
{
  return binl2b64(core_md5(str2binl(s), s.length * chrsz));
}

function str_md5(s)
{
  return binl2str(core_md5(str2binl(s), s.length * chrsz));
}

function hex_hmac_md5(key, data)
{
  return binl2hex(core_hmac_md5(key, data));
}

function b64_hmac_md5(key, data)
{
  return binl2b64(core_hmac_md5(key, data));
}

function str_hmac_md5(key, data)
{
  return binl2str(core_hmac_md5(key, data));
}

function md5_vm_test()
{
  return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}

function core_md5(x, len)
{
  x[len >> 5] |= 0x80 << ((len) % 32);
  x[(((len + 64) >>> 9) << 4) + 14] = len;

  var a = 1732584193;
  var b =  - 271733879;
  var c =  - 1732584194;
  var d = 271733878;

  for (var i = 0; i < x.length; i += 16)
  {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;

    a = md5_ff(a, b, c, d, x[i + 0], 7,  - 680876936);
    d = md5_ff(d, a, b, c, x[i + 1], 12,  - 389564586);
    c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
    b = md5_ff(b, c, d, a, x[i + 3], 22,  - 1044525330);
    a = md5_ff(a, b, c, d, x[i + 4], 7,  - 176418897);
    d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
    c = md5_ff(c, d, a, b, x[i + 6], 17,  - 1473231341);
    b = md5_ff(b, c, d, a, x[i + 7], 22,  - 45705983);
    a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
    d = md5_ff(d, a, b, c, x[i + 9], 12,  - 1958414417);
    c = md5_ff(c, d, a, b, x[i + 10], 17,  - 42063);
    b = md5_ff(b, c, d, a, x[i + 11], 22,  - 1990404162);
    a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
    d = md5_ff(d, a, b, c, x[i + 13], 12,  - 40341101);
    c = md5_ff(c, d, a, b, x[i + 14], 17,  - 1502002290);
    b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);

    a = md5_gg(a, b, c, d, x[i + 1], 5,  - 165796510);
    d = md5_gg(d, a, b, c, x[i + 6], 9,  - 1069501632);
    c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
    b = md5_gg(b, c, d, a, x[i + 0], 20,  - 373897302);
    a = md5_gg(a, b, c, d, x[i + 5], 5,  - 701558691);
    d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
    c = md5_gg(c, d, a, b, x[i + 15], 14,  - 660478335);
    b = md5_gg(b, c, d, a, x[i + 4], 20,  - 405537848);
    a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
    d = md5_gg(d, a, b, c, x[i + 14], 9,  - 1019803690);
    c = md5_gg(c, d, a, b, x[i + 3], 14,  - 187363961);
    b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
    a = md5_gg(a, b, c, d, x[i + 13], 5,  - 1444681467);
    d = md5_gg(d, a, b, c, x[i + 2], 9,  - 51403784);
    c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
    b = md5_gg(b, c, d, a, x[i + 12], 20,  - 1926607734);

    a = md5_hh(a, b, c, d, x[i + 5], 4,  - 378558);
    d = md5_hh(d, a, b, c, x[i + 8], 11,  - 2022574463);
    c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
    b = md5_hh(b, c, d, a, x[i + 14], 23,  - 35309556);
    a = md5_hh(a, b, c, d, x[i + 1], 4,  - 1530992060);
    d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
    c = md5_hh(c, d, a, b, x[i + 7], 16,  - 155497632);
    b = md5_hh(b, c, d, a, x[i + 10], 23,  - 1094730640);
    a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
    d = md5_hh(d, a, b, c, x[i + 0], 11,  - 358537222);
    c = md5_hh(c, d, a, b, x[i + 3], 16,  - 722521979);
    b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
    a = md5_hh(a, b, c, d, x[i + 9], 4,  - 640364487);
    d = md5_hh(d, a, b, c, x[i + 12], 11,  - 421815835);
    c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
    b = md5_hh(b, c, d, a, x[i + 2], 23,  - 995338651);

    a = md5_ii(a, b, c, d, x[i + 0], 6,  - 198630844);
    d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
    c = md5_ii(c, d, a, b, x[i + 14], 15,  - 1416354905);
    b = md5_ii(b, c, d, a, x[i + 5], 21,  - 57434055);
    a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
    d = md5_ii(d, a, b, c, x[i + 3], 10,  - 1894986606);
    c = md5_ii(c, d, a, b, x[i + 10], 15,  - 1051523);
    b = md5_ii(b, c, d, a, x[i + 1], 21,  - 2054922799);
    a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
    d = md5_ii(d, a, b, c, x[i + 15], 10,  - 30611744);
    c = md5_ii(c, d, a, b, x[i + 6], 15,  - 1560198380);
    b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
    a = md5_ii(a, b, c, d, x[i + 4], 6,  - 145523070);
    d = md5_ii(d, a, b, c, x[i + 11], 10,  - 1120210379);
    c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
    b = md5_ii(b, c, d, a, x[i + 9], 21,  - 343485551);

    a = safe_add(a, olda);
    b = safe_add(b, oldb);
    c = safe_add(c, oldc);
    d = safe_add(d, oldd);
  }
  if (mode == 16)
  {
    return Array(b, c);
  }
  else
  {
    return Array(a, b, c, d);
  }
}

function md5_cmn(q, a, b, x, s, t)
{
  return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
}

function md5_ff(a, b, c, d, x, s, t)
{
  return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}

function md5_gg(a, b, c, d, x, s, t)
{
  return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}

function md5_hh(a, b, c, d, x, s, t)
{
  return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}

function md5_ii(a, b, c, d, x, s, t)
{
  return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}

function core_hmac_md5(key, data)
{
  var bkey = str2binl(key);
  if (bkey.length > 16)
    bkey = core_md5(bkey, key.length * chrsz);

  var ipad = Array(16), opad = Array(16);
  for (var i = 0; i < 16; i++)
  {
    ipad[i] = bkey[i] ^ 0x36363636;
    opad[i] = bkey[i] ^ 0x5C5C5C5C;
  }

  var hash = core_md5(ipad.concat(str2binl(data)), 512+data.length * chrsz);
  return core_md5(opad.concat(hash), 512+128);
}

function safe_add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

function bit_rol(num, cnt)
{
  return (num << cnt) | (num  >>> (32-cnt));
}

function str2binl(str)
{
  var bin = Array();
  var mask = (1 << chrsz) - 1;
  for (var i = 0; i < str.length * chrsz; i += chrsz)
    bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
  return bin;
}

function binl2str(bin)
{
  var str = "";
  var mask = (1 << chrsz) - 1;
  for (var i = 0; i < bin.length * 32; i += chrsz)
    str += String.fromCharCode((bin[i >> 5] >>> (i % 32)) & mask);
  return str;
}

function binl2hex(binarray)
{
  var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
  var str = "";

  for (var i = 0; i < binarray.length * 4; i++)
  {
    str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8+4)) & 0xF) +
      hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF);
  }
  return str;
}

function binl2b64(binarray)
{
  var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  var str = "";
  for (var i = 0; i < binarray.length * 4; i += 3)
  {
    var triplet = (((binarray[i >> 2] >> 8 * (i % 4)) & 0xFF) << 16) | ((
      (binarray[i + 1 >> 2] >> 8 * ((i + 1) % 4)) & 0xFF) << 8) | ((binarray[i
      + 2 >> 2] >> 8 * ((i + 2) % 4)) & 0xFF);
    for (var j = 0; j < 4; j++)
    {
      if (i * 8+j * 6 > binarray.length * 32)
        str += b64pad;
      else
        str += tab.charAt((triplet >> 6 * (3-j)) & 0x3F);
    }
  }
  return str;
}
/*************************************************
 * 财付通COOKIE相关处理函数
 *************************************************/
function CCookie()
{
  this.SetCookie = setCookie;
  this.GetCookie = getCookie;
  this.DelCookie = deleteCookie;
}
function getExpDate(days, hours, minutes)
{
    var expDate = new Date( );
    if (typeof days == "number" && typeof hours == "number" &&
        typeof hours == "number")
    {
        expDate.setDate(expDate.getDate( ) + parseInt(days));
        expDate.setHours(expDate.getHours( ) + parseInt(hours));
        expDate.setMinutes(expDate.getMinutes( ) + parseInt(minutes));
        return expDate.toGMTString( );
    }
}

// utility function called by getCookie( )
function getCookieVal(offset)
{
    var endstr = document.cookie.indexOf (";", offset);
    if (endstr == -1)
    {
        endstr = document.cookie.length;
    }
    return unescape(document.cookie.substring(offset, endstr));
}

// primary function to retrieve cookie by name
function getCookie(name)
{
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen)
    {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg)
        {
            return getCookieVal(j);
        }
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return "";
}

// store cookie value with optional details as needed
function setCookie(name, value, expires, path, domain, secure)
{
    document.cookie = name + "=" + escape (value) +
        ((expires) ? "; expires=" + expires : "") +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
}

// remove the cookie by setting ancient expiration date
function deleteCookie(name,path,domain)
{
    if (getCookie(name)) {
        document.cookie = name + "=" +
            ((path) ? "; path=" + path : "") +
            ((domain) ? "; domain=" + domain : "") +
            "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}
/*
 * 定义全局变量g_CCookie
 */
var g_CCookie = null;
if(!g_CCookie)
	g_CCookie = new CCookie();

/*************************************************
 * 财付通数字证书相关处理函数:使用公司QQCERT控件
 *************************************************/
function CQQCert()
{
  //是否使用QQCERT控件
  this.m_bUseFlag = true;
  //控件版本
  this.m_sVersion = "1,0,1,1";
  //控件GUID
  this.m_sClassid = "CLSID:BAEA0695-03A4-43BB-8495-C7025E1A8F42";
  //控件路径
  this.m_sLocate = "https://www.tenpay.com/download/qqcert.cab";
  //绘出控件
	this.Draw = CQQCert_Draw;
	this.Import = CQQCert_Import;
	this.FindCrt = CQQCert_FindCrt;
	this.DelCrt = CQQCert_DelCrt;
	this.MakeCSR = CQQCert_MakeCSR;
	this.Sign = CQQCert_Sign;
	this.HostName = CQQCert_HostName;
	this.Version = CQQCert_Version;
	this.Base64Decode = CQQCert_Base64Decode;
	this.Base64Encode = CQQCert_Base64Encode;
	this.IsObjOk = CQQCert_IsObjOk
	this.IsCnExist = CQQCert_IsCnExist;
}

function CQQCert_Draw(idname, other, htmlparentid)
{
  var strObj;
  if(window.ActiveXObject)
  {
  strObj = "<OBJECT id=\"" + idname + "\" ";
  strObj+= "classid=\"" + this.m_sClassid + "\" ";
  strObj+= " codebase=\"" + this.m_sLocate + "#Version=" + this.m_sVersion + "\" ";
	if(other)
		strObj+=" " + other;
  strObj+= " height=0 width=0 style=\"display:none;\"></OBJECT>";
  }
  else if(navigator.userAgent.toLowerCase().indexOf("firefox") != -1)
  {
	strObj = "<embed id=\"" + idname + "\" type=\"application/qqcert\" name=\"QQCertCtrl\" width=0 height=0></embed>";
  }
  if(htmlparentid)
  {
	  var obj=document.getElementById(htmlparentid);
	  obj.innerHTML=strObj;
  }
  else
  {
  document.write(strObj);
  try{
		if(document.getElementById(idname).Version< parseInt(TFL.cert.VERSION)){
				document.write("<a href=\"/download/qqcert.exe\"><font style=\"font-size:12px\" color=ff6600>未安装数字证书控件？");
				document.write("请点此安装控件后刷新</a>");
			}
		} catch(er) {}
  }
}

function CQQCert_Import(ctl,crt)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return false;
	}
	var ret,re;
	var cerstr;
	ret = ctl.ImportCert(crt);
	if(ret==-1)
	{
    return false;
  }
	return true;
}

function CQQCert_FindCrt(ctl,keyvalue, keyname)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}

	if(!keyname)
		keyname="SN";

  var cn=ctl.FindCert(keyname, keyvalue);
  if(!cn||cn==null||cn=="")
  {
    return "";
  }
  else
  {
    return cn;
  }
}

function CQQCert_DelCrt(ctl,keyvalue,cn, keyname)
{
	var iRet=-1;
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return false;
	}
	if (!cn)
	{
		cn = GetLocalCert(ctl,keyvalue);
	}
	if (cn == "")
	{
		return false;
	}
  try
  {
//    iRet = ctl.DelCert(this.FindCrt(ctl,keyvalue, keyname));
    iRet = ctl.DelCert(cn);
  }
  catch(e)
  {
    return false;
  }
  return (iRet==1);
}

function CQQCert_MakeCSR(ctl,subject,uin,sid)
{

	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}
  var ret,csr;
	ret = ctl.SetChallenge(uin,sid);
	if(ret!=1){
		return "";
	}
	ret = ctl.SetSubject(subject);
	if(ret!=1){
		return "";
	}
	csr = ctl.GetCSR();
	return csr;
}

function CQQCert_Sign(ctl,keyvalue,message,cn,keyname)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}
	if (!cn)
	{
		cn = GetLocalCert(ctl,keyvalue);
	}
	if (cn == "")
	{
		return "";
	}
  return ctl.CertSign(cn,message);
}

function CQQCert_IsCnExist(ctl, cn)
{
	/*
	var str=ctl.CertSign(cn,ctl.Base64Encode("1"));
	if(str && str != "")
		return true;
	else
		return false;
	*/
	var res;
	try
	{	res = ctl.IsCertExist(cn);	}
	catch(e)
	{	res = -1;	}
	if(res == 1)
		return true;
	else
		return false;
}

function CQQCert_HostName(ctl)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}
  return ctl.HostName;
}

function CQQCert_Version(ctl)
{
  return ctl.Version;
}

function CQQCert_Base64Encode(ctl,str)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}
  return ctl.Base64Encode(str);
}

function CQQCert_Base64Decode(ctl,str)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
	{
		return "";
	}
  return ctl.Base64Decode(str);
}

function CQQCert_IsObjOk(ctl)
{
	if (!(TFL.cert.Version(ctl) >= parseInt(TFL.cert.VERSION)))
		return false;
	else
		return true;
}

TFL.cert = new CQQCert();
TFL.cert.VERSION = 1011;

function WriteCertSignCookie(srcstr)
{
if (TFL.cookie.get("certuserflag") != "1")
{
	return true;
}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if (!QQCertCtrl)
	{
		return false;
	}
if (!(TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION)))
{
	if(TFL.cert.m_bUseFlag)
	{
	}
	return false;
}
//	var cn = TFL.cert.FindCrt(QQCertCtrl,TFL.cookie.get("qluid"));
	var cn = GetLocalCert(QQCertCtrl,TFL.cookie.get("qluid"));
	if (cn == "")
	{
		top.location.href = "/certificates/tenpay_safe_tips2.shtml";
		return false;
	}
	g_CCookie.SetCookie(
			"cn",
			cn,
			"",
			"/",
			"tenpay.com",
			"");
	var signstr = TFL.cert.Sign(QQCertCtrl,TFL.cookie.get("qluid"),TFL.cert.Base64Encode(QQCertCtrl,srcstr));
		g_CCookie.SetCookie(
			"srcstr",
			srcstr,
			"",
			"/",
			"tenpay.com",
			"");
		signstr = encodeURIComponent(signstr);
		g_CCookie.SetCookie(
			"signstr",
			signstr,
			"",
			"/",
			"tenpay.com",
			"");
	return true;
}

function WriteCertSignCookie2(CertCtrlId, iUid, sSrc, sSeq)
{

	var QQCertCtrl = window.top.document.getElementById(CertCtrlId);
	if (!QQCertCtrl)
		return false;

	if (!(TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION)))
		return false;

//	var cn = TFL.cert.FindCrt(QQCertCtrl, iUid);
	var cn = GetLocalCert(QQCertCtrl, iUid);
	if (cn == "")
		return false;

	var signstr = TFL.cert.Sign(QQCertCtrl,iUid,TFL.cert.Base64Encode(QQCertCtrl, md5(TFL.cert.Base64Encode(QQCertCtrl,sSeq+sSrc)).toUpperCase()));
	g_CCookie.SetCookie("cn", cn,"","/","tenpay.com","");
	g_CCookie.SetCookie("signseq", sSeq,"","/","tenpay.com","");
	g_CCookie.SetCookie("signstr", signstr,"","/","tenpay.com","");
	return true;
}


function CertStat()
{
	var result = 0;
	if (TFL.cookie.get("certuserflag") == "1")
	{
		result += 1;
	}
	else
	{
		result += 0;
		return result;
	}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if(!QQCertCtrl)
		var QQCertCtrl =TFL.dom.$("QQCertCtrl");

	if (!QQCertCtrl)
	{
		return result;
	}
	if (TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION))
	{
		result += 2;
	}
	else
	{
		result += 0;
		return result;
	}

//	var cn = TFL.cert.FindCrt(QQCertCtrl,TFL.cookie.get("qluid"));
	var cn = GetLocalCert(QQCertCtrl,TFL.cookie.get("qluid"));
	if (cn != "")
	{
		result += 4;
	}
	else
	{
		result += 0;
	}
	return result;
}

function CertUserHasActive()
{
	var result = CertStat();
	if (result == 0)
	{
		return true;
	}
	else if (result == 1)
	{
		return false;
	}
	else if (result == 3 || 7 == result)
	{
		return true;
	}
	else
	{
	// error
		return false;
	}
}

function CertUserHasCert()
{
	var result = CertStat();
	if (result == 0)
	{
		return 0;
	}
	else if (result == 1)
	{
		return 1;
	}
	else if (result == 3)
	{
		return 1;
	}
	else if (7 == result)
	{
		var m_cn = GetLocalCert();
		if (m_cn > 0)
		{
			return 2;
		}
		else
			return 1;
	}
	else
	{
	// error
		return 0;
	}
}

function CertStat_wallet()
{
	var result = 0;
	if ("1" == g_CCftUser.m_sCertuserflag)
	{
		result += 1;
	}
	else
	{
		result += 0;
		return result;
	}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if(!QQCertCtrl)
		var QQCertCtrl =TFL.dom.$("QQCertCtrl");

	if (!QQCertCtrl)
	{
		return result;
	}
	if (TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION))
	{
		result += 2;
	}
	else
	{
		result += 0;
		return result;
	}
//	var cn = TFL.cert.FindCrt(QQCertCtrl,g_CCftUser.m_sInnerid);
	var cn = GetLocalCert(QQCertCtrl,g_CCftUser.m_sInnerid);
	if (cn != "")
	{
		result += 4;
	}
	else
	{
		result += 0;
	}
	return result;
}

function CertUserHasActive_wallet()
{
	var result = CertStat_wallet();
	if (result == 0)
	{
		return true;
	}
	else if (result == 1)
	{
		return false;
	}
	else if (result == 3 || 7 == result)
	{
		return true;
	}
	else
	{
	// error
		return false;
	}
}

function WriteCertSignCookie_wallet(srcstr)
{
	if (g_CCftUser.m_sCertuserflag != "1")
	{
		return true;
	}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if(!QQCertCtrl)
		var QQCertCtrl =TFL.dom.$("QQCertCtrl");

	if (!QQCertCtrl)
	{
		return false;
	}
if (!(TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION)))
{
	if(TFL.cert.m_bUseFlag)
	{
	}
	return false;
}
//	var cn = TFL.cert.FindCrt(QQCertCtrl,g_CCftUser.m_sInnerid);
	var cn = GetLocalCert(QQCertCtrl,g_CCftUser.m_sInnerid);
	if (cn == "")
	{
		TB_show('','#TB_inline?height=150&width=225&inlineId=div_no_cert&ex=noclick_notitle','');
//		top.location.href = "/certificates/tenpay_safe_tips2.shtml";
		return false;
	}
	g_CCookie.SetCookie(
			"cn",
			cn,
			"",
			"/",
			"tenpay.com",
			"");
	var signstr = TFL.cert.Sign(QQCertCtrl,g_CCftUser.m_sInnerid,TFL.cert.Base64Encode(QQCertCtrl,srcstr));
		g_CCookie.SetCookie(
			"srcstr",
			srcstr,
			"",
			"/",
			"tenpay.com",
			"");
		signstr = encodeURIComponent(signstr);
		g_CCookie.SetCookie(
			"signstr",
			signstr,
			"",
			"/",
			"tenpay.com",
			"");
	return true;
}

function CertStat_client()
{
	var result = 0;
	if (TFL.cookie.get("certuserflag") == "1")
	{
		result += 1;
	}
	else
	{
		result += 0;
		return result;
	}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if(!QQCertCtrl)
		QQCertCtrl =TFL.dom.$("QQCertCtrl");
	if (!QQCertCtrl)
	{
		return result;
	}
	if (TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION))
	{
		result += 2;
	}
	else
	{
		result += 0;
		return result;
	}
//	var cn = TFL.cert.FindCrt(QQCertCtrl,TFL.cookie.get("qluid"));
	var cn = GetLocalCert(QQCertCtrl,TFL.cookie.get("qluid"));
	if (cn != "")
	{
		result += 4;
	}
	else
	{
		result += 0;
	}
	return result;
}

function CertUserHasActive_client()
{
	var result = CertStat_client();
	if (result == 0)
	{
		return true;
	}
	else if (result == 1)
	{
		return false;
	}
	else if (result == 3 || 7 == result)
	{
		return true;
	}
	else
	{
	// error
		return false;
	}
}

function WriteCertSignCookie_client(srcstr)
{
if (TFL.cookie.get("certuserflag") != "1")
{
	return true;
}
	var QQCertCtrl = window.top.document.getElementById("QQCertCtrl");
	if (!QQCertCtrl)
	{
		return false;
	}
if (!(TFL.cert.Version(QQCertCtrl) >= parseInt(TFL.cert.VERSION)))
{
	if(TFL.cert.m_bUseFlag)
	{
	}
	return false;
}
//	var cn = TFL.cert.FindCrt(QQCertCtrl,TFL.cookie.get("qluid"));
	var cn = GetLocalCert(QQCertCtrl,TFL.cookie.get("qluid"));
	if (cn == "")
	{
		TB_show('','#TB_inline?height=150&width=225&inlineId=div_no_cert&ex=noclick_notitle','');
//		top.location.href = "/certificates/tenpay_safe_tips2.shtml";
		return false;
	}
	g_CCookie.SetCookie(
			"cn",
			cn,
			"",
			"/",
			"tenpay.com",
			"");
	var signstr = TFL.cert.Sign(QQCertCtrl,TFL.cookie.get("qluid"),TFL.cert.Base64Encode(QQCertCtrl,srcstr));
		g_CCookie.SetCookie(
			"srcstr",
			srcstr,
			"",
			"/",
			"tenpay.com",
			"");
		signstr = encodeURIComponent(signstr);
		g_CCookie.SetCookie(
			"signstr",
			signstr,
			"",
			"/",
			"tenpay.com",
			"");
	return true;
}

function GetLocalCert(ctl,innerid,certlist)
{
	if (!ctl)
	{
		ctl = TFL.dom.$("QQCertCtrl");
		if (!ctl)
			ctl = top.document.getElementById("QQCertCtrl");
	}
	if (!innerid)
	{
		innerid = TFL.cookie.get("qluid");
	}
	if (!certlist)
	{
		certlist = TFL.cookie.get("certlist");
	}
	var cns = certlist.split("-");
	var cn;
	var i = 0;
	var flag = -1;
	for (i=0; i<cns.length; i++)
	{
		if (cns[i] == "")
			continue;
		cn = parseInt(cns[i]);
		try
		{
			flag = TFL.cert.IsCnExist(ctl,cn.toString());
		}
		catch(e)
		{
			flag = -1;
		}
		if (flag == 1)
		{
			// cert exist, get if the cert can be used
			var res= "";
			try
			{
				res = TFL.cert.Sign(
					ctl,
					innerid,
					TFL.cert.Base64Encode(ctl,"0123456789abcdef"),
					cn.toString());
			}
			catch(e)
			{
				res = "";
			}
			if (res != "")
			{
				// cert exist, and it can be used
				return cn + "";
			}
			else
				// cert can't be used, and it is useless
				continue;
		}
	}
	return "";
}

TFL.cert.checkCert = function (uin, noInstall, noCertCtrl) {
  try {
    var QQCertCtrl=TFL.dom.$("QQCertCtrl");
    if(!TFL.cert.IsObjOk(QQCertCtrl)) throw "";
    var cn = GetLocalCert(QQCertCtrl, uin);
    if(cn == "") {
      /*
      var layerTitle = '提示';
      var layerContent = '<span class="cert_caution"><em class="h">警告</em></span><h6>您是财付通数字证书用户，但本机还没有安装数字证书。</h6><p class="cert-info">为保证您的账户安全，需要安装数字证书后才能使用余额支付，您也可以选择使用<a href="javascript:void(0)" onclick="closeLayer();switchTo(1);">网上银行支付</a>。<div class="btn-holder"><span class="btn"><b><input type="button" value="安装证书" onclick="window.open(\'/certificates/tenpay_reinstall1.shtml\')" /></b></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="btn2"><b><input type="button" value="安装成功" onclick="window.location.reload()" /></b></span></div>';
      showLayer(layerTitle,layerContent,"295px");
      */
      if (noInstall) noInstall();
      return false;
    }
    return true;
  }catch (exp) {
    /*
    var layerTitle = '提示';
    var layerContent = '<span class="cert_caution"><em class="h">警告</em></span><h6>您是证书用户，为了您的安全，请先安装安全控件，然后刷新页面！</h6><div class="btn-holder"><span class="btn"><b><input type="button" value="点此下载安全控件安装程序" class="btn" onclick="window.open(\'https://www.tenpay.com/download/qqcert.exe\')" /></b></span></div>';
    showLayer(layerTitle,layerContent,"295px");
    */
    if (noCertCtrl) noCertCtrl();
    return false;
  }
};

TFL.client =
{
	getQQList : function(index)
	{
		if(window.ActiveXObject)
		{
			try
			{
				var rtn = [];
				var PTLoginCtrl = new ActiveXObject("SSOAxCtrlForPTLogin.SSOForPTLogin");
				var vInitData = PTLoginCtrl.CreateTXSSOData();
				PTLoginCtrl.InitSSOFPTCtrl(0, vInitData);
				var vOptData = PTLoginCtrl.CreateTXSSOData();
				var vResult = PTLoginCtrl.DoOperation(1, vOptData);
				if(vResult != null)
				{
					var vAccountList = vResult.GetArray("PTALIST");
					var uListSize = vAccountList.GetSize();
					var strMessage = 'PVAList::';
					for(var uIndex=0;uIndex<uListSize;uIndex++)
					{
						var vAccount = vAccountList.GetData(uIndex);
						var vAccountNameList = vAccount.GetArray("SSO_Account_AccountValueList");
						var vAccountName = vAccountNameList.GetStr(0);
						if(index)
						{
							if(index == -1)
							{
								rtn.push(vAccountName);
							}
							else if(uIndex == index)
							{
								rtn = vAccountName;
								break;
							}
						}
						else
						{
							rtn = vAccountName;
							break;
						}
					}
					return rtn;
				 }
				 else
				 {
					return "";
				 }
			 }
			 catch(oError)
			 {
				return "";
			 }
		 }
		 else
		 {
			return "";
		 }
	}
}
TFL.reportError=(function(){var _cgi="https://www.tenpay.com/cgi-bin/v1.0/report_error.cgi";var _clearTime=3000;var initSender=function(){var _img=new Image();return _img;};var getCookieItem=function(str){var _len=TFL.cookie.get("qlerrinfo");var _rtn="";if(_len){var _arr1=_len.split("|");for(var i=0;i<_arr1.length;i++){var _arr2=_arr1[i].split(":");if(_arr2[0]==str){_rtn=_arr2[1];}}}return _rtn;};var errSend=function(obj){var _trans,_type,_name,_errcode,_errmsg,_callback;_type=obj.type;_callback=obj.callback;_errmsg=obj.errmsg;if(_type==1){_trans=getCookieItem("trans");_name=getCookieItem("cgi");_errcode=getCookieItem("code");}else{_trans=obj.trans||"";_name=obj.name||"";_errcode=obj.errcode||"09000000";}setTimeout(function(){var _sender=initSender();_sender.src=_cgi+"?"+"trans="+_trans+"&type="+_type+"&name="+_name+"&errcode="+_errcode+"&errmsg="+_errmsg+"&t="+new Date().getTime();clearSender(_sender);},1)};var clearSender=function(sender){setTimeout(function(){sender=null},_clearTime);};var getErrMsg=function(errCode){if(typeof(gErrorSet)!="undefined"&&typeof(gErrorSet[errCode])!="undefined"&&typeof(gErrorSet[errCode]["comment"])!="undefined"){return gErrorSet[errCode]["comment"];}return null;};return{send:errSend,getMsg:getErrMsg};})();
TFL.control = {
	trim : function(s){
		return s.replace(/(^\s*)|(\s*$)/g, "");//去左右空格
	},
	lower : function(s){
		return s.toLowerCase();//转小写
	},
	mail : {
		re : /^[_\-\.a-zA-Z0-9]+@([_\-a-zA-Z0-9]+\.)+[a-zA-Z0-9]{2,3}$/,
		format : function(s){
			s = TFL.control.trim(s);
			if(!TFL.control.mail.re.test(s)){
				return false;
			}
			var _s = s.split(/\@/)[1];
			_s = TFL.control.lower(_s);
			s = s.split(/\@/)[0] + "@" + _s;
			return s;
		}
	},
	creID : {
		checkID18 : function(s){
			var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
			var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
			var nTemp = 0, i;
			if(s.length==18){
				for(i = 0; i < s.length-1; i ++){
					nTemp += s.substr(i, 1) * arrInt[i];
				}
				if(s.substr(17, 1).toUpperCase() != arrCh[nTemp % 11]){
					return false;
				}
			}
			return true;
		},
		checkHkId : function(s){
			return false;
		},
		format : function(s){
			s = TFL.control.trim(s);
			var patn = /^([0-9(x|X)]{15,15})$/;
			var patn1 = /^([0-9(x|X)]{18,18})$/;
			if(patn.test(s) || patn1.test(s)|| TFL.control.creID.checkHkId(s)){
				if(TFL.control.creID.checkID18(s)){
					s = TFL.control.lower(s);
					return s;
				}else{
					return false;
				}
			}
			return false;
		}
	}
};