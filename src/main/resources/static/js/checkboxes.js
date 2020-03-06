$('.css-checkbox').click(function(){
    $('.css-checkbox').each(function(){
        $(this).prop('checked', false);
    });
    $(this).prop('checked', true);
});
