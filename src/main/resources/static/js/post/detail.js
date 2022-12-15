//top anchor smooth scroll
$('#top').click(function() {
  $('html, body').animate({
    scrollTop: 0
  }, 400);
  return false;
});


// 글자수 세기
$(document).ready(function() {
  $('#texts').on('keyup', function() {
    $('#overs').html("(" + $(this).val().length + "/2000" + "자)");

    if ($(this).val().length > 2000) {
      $(this).val($(this).val().substring(0, 2000));
      $('#overs').html("(2000 / 2000" + "자)");
    }
  });
});


//댓글 - 드롭다운 신고 버튼
function openToc1() {
  document.getElementById('more1').classList.toggle('xx');
}

function openToc2() {
  document.getElementById('more2').classList.toggle('xx');
}


//qna 아코디언
var $faq = $('.qna-list');
var $faq_question = $faq.find('li .head');

$faq_question.on('click', function(e) {
  e.preventDefault();

  $faq.find('.cont').slideUp();
  $faq.find('.question').attr('title', '내용 보기');
  $faq_question.removeClass('active');

  if (!$(this).next().is(':visible')) {
    $(this).next().slideDown();
    $(this).find('.question').attr('title', '내용 닫기');
    $(this).addClass('active');
  }
});




//FAQ 아코디언
$(".que").click(function() {
      $(this).next(".ans").stop().slideToggle(300);
      $(this).toggleClass('on').siblings().removeClass('on');
      $(this).next(".ans").siblings(".ans").slideUp(300); // 1개씩 펼치기
    });
