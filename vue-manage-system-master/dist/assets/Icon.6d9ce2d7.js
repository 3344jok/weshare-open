import{_ as l}from"./index.d317ee26.js";import{E as e,k as i,r as a,o,b as t,e as s,f as n,w as c,t as r,F as d,l as f,p,i as u,h as m,a8 as v,n as h}from"./vendor.f3d64f44.js";const k={name:"icon",setup(){const l=["attentionforbid","attentionforbidfill","attention","attentionfill","tag","tagfill","people","peoplefill","notice","noticefill","mobile","mobilefill","voice","voicefill","unlock","lock","home","homefill","delete","deletefill","notification","notificationfill","notificationforbidfill","like","likefill","comment","commentfill","camera","camerafill","warn","warnfill","time","timefill","location","locationfill","favor","favorfill","skin","skinfill","news","newsfill","record","recordfill","emoji","emojifill","message","messagefill","goods","goodsfill","crown","crownfill","move","add","hot","hotfill","service","servicefill","present","presentfill","pic","picfill","rank","rankfill","male","female","down","top","recharge","rechargefill","forward","forwardfill","info","infofill","redpacket","redpacket_fill","roundadd","roundaddfill","friendadd","friendaddfill","cart","cartfill","more","moreandroid","back","right","shop","shopfill","question","questionfill","roundclose","roundclosefill","roundcheck","roundcheckfill","global","mail","punch","exit","upload","read","file","link","full","group","friend","profile","addressbook","calendar","text","copy","share","wifi","vipcard","weibo","remind","refresh","filter","settings","scan","qrcode","cascades","apps","sort","searchlist","search","edit"],a=e(""),o=i((()=>l.filter((l=>-1!==l.indexOf(a.value)))));return{iconList:l,keyword:a,list:o}}},x=l=>(p("data-v-52511300"),l=l(),u(),l),g={class:"crumbs"},b=x((()=>s("i",{class:"el-icon-lx-emoji"},null,-1))),w=m(" 自定义图标 "),q={class:"container"},y=x((()=>s("h2",null,"使用方法",-1))),_={style:{"line-height":"50px"}},j=v('<p class="example-p" data-v-52511300><i class="el-icon-lx-redpacket_fill" style="font-size:30px;color:#ff5900;" data-v-52511300></i><span data-v-52511300>&lt;i class=&quot;el-icon-lx-redpacket_fill&quot;&gt;&lt;/i&gt;</span></p><p class="example-p" data-v-52511300><i class="el-icon-lx-weibo" style="font-size:30px;color:#fd5656;" data-v-52511300></i><span data-v-52511300>&lt;i class=&quot;el-icon-lx-weibo&quot;&gt;&lt;/i&gt;</span></p><p class="example-p" data-v-52511300><i class="el-icon-lx-emojifill" style="font-size:30px;color:#ffc300;" data-v-52511300></i><span data-v-52511300>&lt;i class=&quot;el-icon-lx-emojifill&quot;&gt;&lt;/i&gt;</span></p><br data-v-52511300><h2 data-v-52511300>图标</h2>',5),z={class:"search-box"},V={class:"icon-li-content"};var L=l(k,[["render",function(l,e,i,p,u,m){const v=a("el-breadcrumb-item"),k=a("el-breadcrumb"),x=a("el-input");return o(),t("div",null,[s("div",g,[n(k,{separator:"/"},{default:c((()=>[n(v,null,{default:c((()=>[b,w])),_:1})])),_:1})]),s("div",q,[y,s("p",_,"直接通过设置类名为 el-icon-lx-iconName 来使用即可。例如：（共"+r(p.iconList.length)+"个图标）",1),j,s("div",z,[n(x,{class:"search",size:"large",modelValue:p.keyword,"onUpdate:modelValue":e[0]||(e[0]=l=>p.keyword=l),clearable:"",placeholder:"请输入图标名称"},null,8,["modelValue"])]),s("ul",null,[(o(!0),t(d,null,f(p.list,((l,e)=>(o(),t("li",{class:"icon-li",key:e},[s("div",V,[s("i",{class:h(`el-icon-lx-${l}`)},null,2),s("span",null,r(l),1)])])))),128))])])])}],["__scopeId","data-v-52511300"]]);export{L as default};
