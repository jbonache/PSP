onload = function() {

    let button = document.querySelector("#pressMeBtn");
    button.addEventListener("click", function() {
        let imgtop = document.querySelector("#imatges img.top");
        imgtop.classList.toggle("transparent");
        //imgtop.toggleClass("transparent");
        //document.querySelector("#imatges img.top").remove();
    });




}
