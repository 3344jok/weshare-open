var t=Object.defineProperty,e=Object.getOwnPropertySymbols,i=Object.prototype.hasOwnProperty,a=Object.prototype.propertyIsEnumerable,s=(e,i,a)=>i in e?t(e,i,{enumerable:!0,configurable:!0,writable:!0,value:a}):e[i]=a;import{D as o,o as h,b as n,e as l}from"./vendor.f3d64f44.js";import{_ as r}from"./index.d317ee26.js";var d={exports:{}},c=d.exports=function(){function t(t,e){for(var i=0;i<e.length;i++){var a=e[i];a.enumerable=a.enumerable||!1,a.configurable=!0,"value"in a&&(a.writable=!0),Object.defineProperty(t,a.key,a)}}function e(t){return function(t){if(Array.isArray(t)){for(var e=0,i=new Array(t.length);e<t.length;e++)i[e]=t[e];return i}}(t)||function(t){if(Symbol.iterator in Object(t)||"[object Arguments]"===Object.prototype.toString.call(t))return Array.from(t)}(t)||function(){throw new TypeError("Invalid attempt to spread non-iterable instance")}()}var i=window.devicePixelRatio||1,a=10*i,s=a/2;return function(){function o(t,e){var h,n,l,r;!function(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}(this,o),this.canvas=(h=t,n=document.getElementById(h),l=n.parentNode.clientWidth,r=n.parentNode.clientHeight,n.style.width=l+"px",n.style.height=r+"px",n.width=l*i,n.height=r*i,n),this.ctx=this.canvas.getContext("2d"),this.type="bar",this.showValue=!0,this.showGrid=!0,this.topPadding=60*i,this.leftPadding=50*i,this.rightPadding=10*i,this.bottomPadding=50*i,this.yEqual=5,this.yLength=0,this.xLength=0,this.ySpace=0,this.xRorate=0,this.yRorate=0,this.xRotate=0,this.yRotate=0,this.bgColor="#fff",this.axisColor="#666",this.gridColor="#eee",this.title={text:"",color:"#666",position:"top",font:"bold "+18*i+"px Arial",top:a,bottom:s},this.legend={display:!0,position:"top",color:"#666",font:14*i+"px Arial",top:45*i,bottom:15*i,textWidth:0},this.radius=100*i,this.innerRadius=60*i,this.colorList=["#4A90E2","#F5A623","#ff5858","#5e64ff","#2AC766","#743ee2","#b554ff","#199475"],this.init(e)}return function(e,i,a){i&&t(e.prototype,i),a&&t(e,a)}(o,[{key:"init",value:function(t){if(t.title=Object.assign({},this.title,t.title),t.legend=Object.assign({},this.legend,t.legend),Object.assign(this,t),!t.labels||!t.labels.length)throw new Error("缺少主要参数labels");if(!t.datasets||!t.datasets.length)throw new Error("缺少主要参数datasets");this.drawBackground(),"bar"===this.type||"line"===this.type?this.renderBarChart():this.renderPieChart(),this.drawLegend()}},{key:"renderBarChart",value:function(){this.yLength=Math.floor((this.canvas.height-this.topPadding-this.bottomPadding-a)/this.yEqual),this.xLength=Math.floor((this.canvas.width-this.leftPadding-this.rightPadding-a)/this.labels.length),this.ySpace=function(t,i){var a=t.map((function(t){return t.data.reduce((function(t,e){return e<t?t:e}))})),s=Math.ceil(Math.max.apply(Math,e(a))/i),o=s.toString().length-1;return o=2<o?2:o,Math.ceil(s/Math.pow(10,o))*Math.pow(10,o)}(this.datasets,this.yEqual),this.drawXAxis(),this.drawYAxis(),this.drawBarContent()}},{key:"drawBarContent",value:function(){var t=this.ctx,e=this.datasets.length;t.beginPath();for(var o=0;o<e;o++){t.font=this.legend.font,this.legend.textWidth+=Math.ceil(t.measureText(this.datasets[o].label).width),t.fillStyle=t.strokeStyle=this.datasets[o].fillColor||this.colorList[o];for(var h=this.datasets[o].data,n=0;n<h.length;n++)if(!(n>this.labels.length-1)){var l=this.xLength/(e+1),r=this.yLength/this.ySpace,d=this.leftPadding+this.xLength*n+l*(o+.5),c=d+l,f=this.canvas.height-this.bottomPadding,g=f-h[n]*r;if("bar"===this.type)t.fillRect(d,g,c-d,f-g),this.drawValue(h[n],d+l/2,g-s);else if("line"===this.type){var v=this.leftPadding+this.xLength*(n+.5);t.beginPath(),t.arc(v,g,3*i,0,2*Math.PI,!0),t.fill(),0!==n&&(t.beginPath(),t.strokeStyle=this.datasets[o].fillColor||this.colorList[o],t.lineWidth=2*i,t.moveTo(v-this.xLength,f-h[n-1]*r),t.lineTo(v,g),t.stroke(),t.lineWidth=1*i),this.drawValue(h[n],v,g-a)}}}t.stroke()}},{key:"renderPieChart",value:function(){for(var t=this.ctx,e=this.labels.length,i=this.datasets[0],a=i.data,s=a.reduce((function(t,e){return t+e})),o=-Math.PI/2,h=this.canvas.width/2,n=this.canvas.height/2,l=0;l<e;l++){t.font=this.legend.font,this.legend.textWidth+=Math.ceil(t.measureText(this.labels[l]).width),t.beginPath(),t.strokeStyle=t.fillStyle=i.colorList&&i.colorList[l]||this.colorList[l],t.moveTo(h,n);var r=o,d=o+=a[l]/s*2*Math.PI;t.arc(h,n,this.radius,r,d),t.closePath(),t.fill();var c=(r+d)/2;this.drawPieValue(a[l],c)}"ring"===this.type&&(t.beginPath(),t.fillStyle=this.bgColor,t.arc(h,n,this.innerRadius,0,2*Math.PI),t.closePath(),t.fill())}},{key:"drawValue",value:function(t,e,a){var s=this.ctx;this.showValue&&(s.textBaseline="middle",s.font=12*i+"px Arial",s.textAlign="center",s.fillText(t,e,a))}},{key:"drawPieValue",value:function(t,e){var i=this.ctx;if(this.showValue){var s=this.canvas.width/2,o=this.canvas.height/2,h=Math.ceil(Math.abs(this.radius*Math.cos(e))),n=Math.floor(Math.abs(this.radius*Math.sin(e)));i.textBaseline="middle",this.showValue&&(e<=0?(i.textAlign="left",i.moveTo(s+h,o-n),i.lineTo(s+h+a,o-n-a),i.moveTo(s+h+a,o-n-a),i.lineTo(s+h+3*a,o-n-a),i.stroke(),i.fillText(t,s+h+3.5*a,o-n-a)):0<e&&e<=Math.PI/2?(i.textAlign="left",i.moveTo(s+h,o+n),i.lineTo(s+h+a,o+n+a),i.moveTo(s+h+a,o+n+a),i.lineTo(s+h+3*a,o+n+a),i.stroke(),i.fillText(t,s+h+3.5*a,o+n+a)):e>Math.PI/2&&e<Math.PI?(i.textAlign="right",i.moveTo(s-h,o+n),i.lineTo(s-h-a,o+n+a),i.moveTo(s-h-a,o+n+a),i.lineTo(s-h-3*a,o+n+a),i.stroke(),i.fillText(t,s-h-3.5*a,o+n+a)):(i.textAlign="right",i.moveTo(s-h,o-n),i.lineTo(s-h-a,o-n-a),i.moveTo(s-h-a,o-n-a),i.lineTo(s-h-3*a,o-n-a),i.stroke(),i.fillText(t,s-h-3.5*a,o-n-a)))}}},{key:"drawBackground",value:function(){this.ctx.fillStyle=this.bgColor,this.ctx.fillRect(0,0,this.canvas.width,this.canvas.height),this.drawTitle()}},{key:"drawTitle",value:function(){var t=this.title;if(t.text){var e=this.ctx;e.beginPath(),e.font=t.font,e.textAlign="center",e.fillStyle=t.color,"top"===t.position?(e.textBaseline="top",e.fillText(t.text,this.canvas.width/2,t.top)):(e.textBaseline="bottom",e.fillText(t.text,this.canvas.width/2,this.canvas.height-t.bottom))}}},{key:"drawXAxis",value:function(){var t=this.ctx,e=this.canvas.height-this.bottomPadding+.5;t.beginPath(),t.strokeStyle=this.axisColor,t.moveTo(this.leftPadding,e),t.lineTo(this.canvas.width-this.rightPadding,e),t.stroke(),this.drawXPoint()}},{key:"drawXPoint",value:function(){var t=this.ctx;t.beginPath(),t.font=12*i+"px Microsoft YaHei",t.textAlign=this.xRorate||this.xRotate?"right":"center",t.textBaseline="top",t.fillStyle=this.axisColor;for(var e=0;e<this.labels.length;e++){var o=this.labels[e],h=this.leftPadding+this.xLength*(e+1)+.5,n=this.canvas.height-this.bottomPadding;this.showGrid?(t.strokeStyle=this.gridColor,t.moveTo(h,n),t.lineTo(h,this.topPadding+a)):(t.moveTo(h,n),t.lineTo(h,n-s)),t.stroke(),t.save(),t.translate(h-this.xLength/2,n+s),this.xRorate?t.rotate(-this.xRorate*Math.PI/180):t.rotate(-this.xRotate*Math.PI/180),t.fillText(o,0,0),t.restore()}}},{key:"drawYAxis",value:function(){var t=this.ctx;t.beginPath(),t.strokeStyle=this.axisColor,t.moveTo(this.leftPadding-.5,this.canvas.height-this.bottomPadding+.5),t.lineTo(this.leftPadding-.5,this.topPadding+.5),t.stroke(),this.drawYPoint()}},{key:"drawYPoint",value:function(){var t=this.ctx;t.font=12*i+"px Microsoft YaHei",t.textAlign="right",t.textBaseline="middle",t.beginPath();for(var e=0;e<this.yEqual;e++){var o=this.leftPadding,h=this.canvas.height-this.bottomPadding-this.yLength*(e+1)+.5;this.showGrid?(t.strokeStyle=this.gridColor,t.moveTo(o,h),t.lineTo(this.canvas.width-this.rightPadding-a,h)):(t.strokeStyle=this.axisColor,t.moveTo(o-s,h),t.lineTo(o,h)),t.stroke(),t.save(),t.fillStyle=this.axisColor,t.translate(o-a,h),this.yRorate?t.rotate(-this.yRorate*Math.PI/180):t.rotate(-this.yRotate*Math.PI/180),t.fillText(this.ySpace*(e+1),0,0),t.restore()}}},{key:"drawLegend",value:function(){var t=this.legend;if(t.display){var e=this.ctx,i="pie"===this.type||"ring"===this.type;e.beginPath(),e.font=t.font,e.textAlign="left",e.textBaseline="middle";for(var o=i?this.labels.length:this.datasets.length,h=(this.canvas.width-(this.legend.textWidth+(5*o-2)*a))/2,n=0,l=0;l<o;l++){var r=i?this.datasets[0]:this.datasets[l],d=(i?this.labels[l]:r.label)||"";e.fillStyle=r.colorList&&r.colorList[l]||r.fillColor||this.colorList[l],"top"===t.position?(this.drawLegendIcon(h+5*a*l+n,t.top-s,2*a,a),e.fillStyle=t.color,e.fillText(d,h+(5*l+3)*a+n,t.top)):"bottom"===t.position?(this.drawLegendIcon(h+5*a*l+n,this.canvas.height-t.bottom-s,2*a,a),e.fillStyle=t.color,e.fillText(d,h+(5*l+3)*a+n,this.canvas.height-t.bottom)):(e.fillRect(a,t.top+2*a*l,2*a,a),e.fillStyle=t.color,e.fillText(d,4*a,t.top+2*a*l+.5*a)),n+=Math.ceil(e.measureText(d).width)}}}},{key:"drawLegendIcon",value:function(t,e,o,h){var n=this.ctx;"line"===this.type?(n.beginPath(),n.strokeStyle=n.fillStyle,n.lineWidth=2*i,n.moveTo(t,e+s),n.lineTo(t+2*a,e+s),n.stroke(),n.lineWidth=1*i,n.arc(t+a,e+s,3*i,0,2*Math.PI,!0),n.fill()):n.fillRect(t,e,o,h)}}]),o}()}();const f=["id"];var g=r({props:{canvasId:{type:String,default:"",required:!0},options:{type:Object,required:!0}},mounted(){this.renderChart()},methods:{renderChart(){if(!this.checkOptions())return;const t=((t,o)=>{for(var h in o||(o={}))i.call(o,h)&&s(t,h,o[h]);if(e)for(var h of e(o))a.call(o,h)&&s(t,h,o[h]);return t})({},this.options);new c(this.canvasId,t)},checkOptions(){const t=this.options;return!(!t.datasets||!t.datasets.length)&&!(!t.labels||!t.labels.length)}},watch:{options:{handler(t,e){this.renderChart()},deep:!0}}},[["render",function(t,e,i,a,s,o){return h(),n("div",null,[l("canvas",{id:i.canvasId},null,8,f)])}]]);export{g as S};
