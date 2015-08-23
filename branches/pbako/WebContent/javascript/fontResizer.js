var min=12;
var max=22;
var med=16;
var logoSize=50;
function increaseFontSize() {
   var p = document.getElementsByTagName('td');
   var c = document.getElementById("sizeController");
   var logo = document.getElementById("headerLogo");
   for(i=0;i<p.length;i++) {
      if(p[i].style.fontSize) {
         var s = parseInt(p[i].style.fontSize.replace("px",""));
      } else {
         var s = 16;
      }
      if(s!=max) {
         s += 2;
      }
      p[i].style.fontSize = s+"px";
   }
   c.style.fontSize = med+"px";
   logo.style.fontSize = logoSize+"px";
}

function decreaseFontSize() {
   var p = document.getElementsByTagName('td');
   var c = document.getElementById("sizeController");
   var logo = document.getElementById("headerLogo");
   for(i=0;i<p.length;i++) {
      if(p[i].style.fontSize) {
         var s = parseInt(p[i].style.fontSize.replace("px",""));
      } else {
         var s = 14;
      }
      if(s!=min) {
         s -= 2;
      }
      p[i].style.fontSize = s+"px"
   }   
   c.style.fontSize = med+"px";
   logo.style.fontSize = logoSize+"px";
}
