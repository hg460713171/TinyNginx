"use strict";(self["webpackChunkaurora_blog"]=self["webpackChunkaurora_blog"]||[]).push([[818],{5758:function(e,a,t){t.d(a,{Z:function(){return d}});var n=t(3396);const r={class:"flex-shrink-0"},l={class:"rounded-full ring-gray-100 overflow-hidden shaodw-lg w-9 xl:w-10"},i=["src"],s=["src"];function o(e,a,t,o,g,c){return(0,n.wg)(),(0,n.iD)("div",r,[(0,n._)("div",l,[null!=e.url?((0,n.wg)(),(0,n.iD)("img",{key:0,class:"avatar-img",src:e.url,alt:""},null,8,i)):((0,n.wg)(),(0,n.iD)("img",{key:1,class:"avatar-img",src:e.default,alt:""},null,8,s))])])}var g=t(4870),c=(0,n.aZ)({name:"Avatar",props:["url"],setup(e){return{url:(0,g.BK)(e).url,default:"https://static.linhaojun.top/aurora/config/52a81cd2772167b645569342e81ce312.jpg"}}}),u=t(89);const p=(0,u.Z)(c,[["render",o],["__scopeId","data-v-494ebf64"]]);var d=p},7919:function(e,a,t){t.d(a,{Z:function(){return u}});var n=t(3396),r=t(7139);const l={class:"breadcrumbs flex flex-row gap-6 text-white"};function i(e,a,t,i,s,o){return(0,n.wg)(),(0,n.iD)("ul",l,[(0,n._)("li",null,(0,r.zw)(e.t("menu.home")),1),(0,n._)("li",null,(0,r.zw)(e.current),1)])}var s=t(5213),o=(0,n.aZ)({name:"Breadcrumb",props:["current"],setup(){const{t:e}=(0,s.QT)();return{t:e}}}),g=t(89);const c=(0,g.Z)(o,[["render",i],["__scopeId","data-v-64013c38"]]);var u=c},6664:function(e,a,t){t.d(a,{Z:function(){return d}});var n=t(3396),r=t(7139);const l={class:"paginator"},i=["onClick"];function s(e,a,t,s,o,g){const c=(0,n.up)("svg-icon");return(0,n.wg)(),(0,n.iD)("div",l,[(0,n._)("ul",null,[e.currentPage>1?((0,n.wg)(),(0,n.iD)("li",{key:0,class:"text-ob-bright",onClick:a[0]||(a[0]=a=>e.pageChangeEmitter(e.currentPage-1))},[(0,n.Wm)(c,{"icon-class":"arrow-left"}),(0,n.Uk)(" "+(0,r.zw)(e.t("settings.paginator.newer")),1)])):(0,n.kq)("",!0),0!==e.paginator.head?((0,n.wg)(),(0,n.iD)("li",{key:1,class:(0,r.C_)({active:e.currentPage===e.paginator.head}),onClick:a[1]||(a[1]=a=>e.pageChangeEmitter(e.paginator.head))},(0,r.zw)(e.paginator.head),3)):(0,n.kq)("",!0),((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(e.paginator.pages,((a,t)=>((0,n.wg)(),(0,n.iD)("li",{key:t,class:(0,r.C_)({active:e.currentPage===a}),onClick:t=>e.pageChangeEmitter(a)},(0,r.zw)(a),11,i)))),128)),0!==e.paginator.end?((0,n.wg)(),(0,n.iD)("li",{key:2,class:(0,r.C_)({active:e.currentPage===e.paginator.end}),onClick:a[2]||(a[2]=a=>e.pageChangeEmitter(e.paginator.end))},(0,r.zw)(e.paginator.end),3)):(0,n.kq)("",!0),e.currentPage<e.pages?((0,n.wg)(),(0,n.iD)("li",{key:3,class:"text-ob-bright",onClick:a[3]||(a[3]=a=>e.pageChangeEmitter(e.currentPage+1))},[(0,n.Uk)((0,r.zw)(e.t("settings.paginator.older"))+" ",1),(0,n.Wm)(c,{"icon-class":"arrow-right"})])):(0,n.kq)("",!0)])])}var o=t(4870),g=t(5213),c=(0,n.aZ)({name:"Paginator",emits:["pageChange"],props:["pageTotal","pageSize","page"],setup(e,{emit:a}){const{t:t}=(0,g.QT)(),r=(0,o.BK)(e),l=(0,n.Fl)((()=>Math.ceil(r.pageTotal.value/r.pageSize.value))),i=(0,n.Fl)((()=>{if(l.value<=3){const e=[];for(let a=0;a<l.value;a++)e.push(a+1);return{head:0,pages:e,end:0}}return r.page.value>=1&&r.page.value<3?{head:1,pages:[2,3,"..."],end:l.value}:r.page.value>=3&&r.page.value<=l.value-2?{head:1,pages:["...",r.page.value-1,r.page.value,r.page.value+1,"..."],end:l.value}:{head:1,pages:["...",l.value-2,l.value-1],end:l.value}})),s=e=>{"..."!==e&&a("pageChange",e)};return{currentPage:(0,n.Fl)((()=>r.page.value)),pageChangeEmitter:s,paginator:i,pages:l,t:t}}}),u=t(89);const p=(0,u.Z)(c,[["render",s],["__scopeId","data-v-9fdb35d6"]]);var d=p},3840:function(e,a,t){t.r(a),t.d(a,{default:function(){return q}});var n=t(3396),r=t(7139),l=t(9242);const i=e=>((0,n.dD)("data-v-05462efc"),e=e(),(0,n.Cn)(),e),s={class:"flex flex-col"},o={class:"post-header"},g={class:"post-title text-white uppercase"},c={class:"main-grid"},u={class:"relative space-y-5"},p=["onClick"],d={class:"talk-info"},m={class:"user-nickname text-sm"},v={class:"time"},w=i((()=>(0,n._)("span",{style:{color:"#f21835"}},"置顶",-1))),k=["innerHTML"],h={class:"col-span-1"};function f(e,a,t,i,f,_){const C=(0,n.up)("Breadcrumb"),D=(0,n.up)("Avatar"),z=(0,n.up)("svg-icon"),b=(0,n.up)("el-image"),T=(0,n.up)("el-col"),x=(0,n.up)("el-row"),P=(0,n.up)("Paginator"),Z=(0,n.up)("Profile"),y=(0,n.up)("Sidebar");return(0,n.wg)(),(0,n.iD)("div",null,[(0,n.Wm)(C,{current:e.t("menu.talks")},null,8,["current"]),(0,n._)("div",s,[(0,n._)("div",o,[(0,n._)("h1",g,(0,r.zw)(e.t("titles.talks")),1)]),(0,n._)("div",c,[(0,n._)("div",u,[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(e.talks,(a=>((0,n.wg)(),(0,n.iD)("div",{class:"bg-ob-deep-800 flex p-4 lg:p-8 rounded-2xl shadow-xl mb-0 talk-item",key:a.id,onClick:t=>e.toTalk(a.id)},[(0,n.Wm)(D,{url:a.avatar},null,8,["url"]),(0,n._)("div",d,[(0,n._)("div",m,(0,r.zw)(a.nickname),1),(0,n._)("div",v,[(0,n.Uk)((0,r.zw)(e.t("settings.shared-on"))+" "+(0,r.zw)(e.formatTime(a.createTime))+", "+(0,r.zw)(e.t(`settings.months[${new Date(a.createTime).getMonth()}]`))+" "+(0,r.zw)(new Date(a.createTime).getDate())+", "+(0,r.zw)(new Date(a.createTime).getFullYear())+" ",1),1===a.isTop?((0,n.wg)(),(0,n.iD)(n.HY,{key:0},[(0,n.Wm)(z,{"icon-class":"top",class:"top-svg"}),w],64)):(0,n.kq)("",!0),(0,n.Wm)(z,{"icon-class":"message",class:"message-svg"}),(0,n.Uk)((0,r.zw)(null==a.commentCount?0:a.commentCount),1)]),(0,n._)("div",{class:"talk-content",innerHTML:a.content},null,8,k),a.imgs?((0,n.wg)(),(0,n.j4)(x,{key:0,class:"talk-images"},{default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(a.imgs,((a,t)=>((0,n.wg)(),(0,n.j4)(T,{md:4,key:t},{default:(0,n.w5)((()=>[(0,n.Wm)(b,{class:"images-items",src:a,"aspect-ratio":"1","max-height":"200",onClick:(0,l.iM)((t=>e.handlePreview(a)),["stop"])},null,8,["src","onClick"])])),_:2},1024)))),128))])),_:2},1024)):(0,n.kq)("",!0)])],8,p)))),128)),(0,n.Wm)(P,{pageSize:e.pagination.size,pageTotal:e.pagination.total,page:e.pagination.current,onPageChange:e.pageChangeHanlder},null,8,["pageSize","pageTotal","page","onPageChange"])]),(0,n._)("div",h,[(0,n.Wm)(y,null,{default:(0,n.w5)((()=>[(0,n.Wm)(Z)])),_:1})])])])])}var _=t(4870),C=t(5213),D=t(7919),z=t(2506),b=t(6664),T=t(5758),x=t(6883),P=t(2483),Z=t(3286),y=(0,n.aZ)({name:"talkList",components:{Breadcrumb:D.Z,Sidebar:z.YE,Profile:z.NZ,Paginator:b.Z,Avatar:T.Z},setup(){const{t:e}=(0,C.QT)(),a=(0,P.tv)(),t=(0,_.qj)({size:7,total:0,current:1}),r=(0,_.qj)({images:[],talks:""});(0,n.bv)((()=>{i()}));const l=e=>{(0,x.e)({images:r.images,index:r.images.indexOf(e)})},i=()=>{const e={current:t.current,size:t.size};Z.Z.getTalks(e).then((({data:e})=>{r.talks=e.data.records,t.total=e.data.count,r.talks.forEach((e=>{e.imgs&&r.images.push(...e.imgs)}))}))},s=e=>{let a=new Date(e).getHours(),t=new Date(e).getMinutes(),n=new Date(e).getSeconds();return a+":"+t+":"+n},o=()=>{window.scrollTo({top:0})},g=e=>{r.talks="",o(),t.current=e,i()},c=e=>{a.push({path:"/talks/"+e})};return{pagination:t,...(0,_.BK)(r),formatTime:s,pageChangeHanlder:g,handlePreview:l,toTalk:c,t:e}}}),W=t(89);const H=(0,W.Z)(y,[["render",f],["__scopeId","data-v-05462efc"]]);var q=H}}]);