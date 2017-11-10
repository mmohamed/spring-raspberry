var CURRENT_URL = window.location.href.split('#')[0].split('?')[0],
    $BODY = $('body'),
    $MENU_TOGGLE = $('#menu_toggle'),
    $SIDEBAR_MENU = $('#sidebar-menu'),
    $SIDEBAR_FOOTER = $('.sidebar-footer'),
    $LEFT_COL = $('.left_col'),
    $RIGHT_COL = $('.right_col'),
    $NAV_MENU = $('.nav_menu'),
    $FOOTER = $('footer');	

var makeContentHeight = function () {
	$RIGHT_COL.css('min-height', $(window).height());
	var bodyHeight = $BODY.outerHeight(),
		footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
		leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
		contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;
	contentHeight -= $NAV_MENU.height() + footerHeight;
	$RIGHT_COL.css('min-height', contentHeight);
};

$SIDEBAR_MENU.find('a').on('click', function(ev) {
    var $li = $(this).parent();
    if ($li.is('.active')) {
        $li.removeClass('active active-sm');
        $('ul:first', $li).slideUp(function() {
            makeContentHeight();
        });
    } else {
        if (!$li.parent().is('.child_menu')) {
            $SIDEBAR_MENU.find('li').removeClass('active active-sm');
            $SIDEBAR_MENU.find('li ul').slideUp();
        }else
        {
			if ( $BODY.is( ".nav-sm" ) )
			{
				$SIDEBAR_MENU.find( "li" ).removeClass( "active active-sm" );
				$SIDEBAR_MENU.find( "li ul" ).slideUp();
			}
		}
        $li.addClass('active');
        $('ul:first', $li).slideDown(function() {
            makeContentHeight();
        });
    }
});
      
$MENU_TOGGLE.on('click', function() {
	if ($BODY.hasClass('nav-md')) {
		$SIDEBAR_MENU.find('li.active ul').hide();
		$SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
	} else {
		$SIDEBAR_MENU.find('li.active-sm ul').show();
		$SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
	}
	$BODY.toggleClass('nav-md nav-sm');
	makeContentHeight();
	$('.dataTable').each ( function () { $(this).dataTable().fnDraw(); });
});

$SIDEBAR_MENU.find('a[href="' + CURRENT_URL + '"]').parent('li').addClass('current-page');

$SIDEBAR_MENU.find('a').filter(function () {
	return this.href == CURRENT_URL;
}).parent('li').addClass('current-page').parents('ul').slideDown(function() {
	makeContentHeight();
}).parent().addClass('active');

$(window).smartresize(function(){  
	makeContentHeight();
});

makeContentHeight();

if ($.fn.mCustomScrollbar) {
	$('.menu_fixed').mCustomScrollbar({
		autoHideScrollbar: true,
		theme: 'minimal',
		mouseWheel:{ preventDefault: true }
	});
}
