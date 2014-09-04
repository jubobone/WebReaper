

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>

</title>
    <style type="text/css">
        body {
            padding: 0;
            margin: 0;           
        }
        /* 730x584 */
        .nt-widget-30 {
            position: absolute;
            left: 4px;
            top: 4px;
            right: 50%;
            padding-right: 2px;
            bottom: 182px;
        }
        .nt-widget-31 {
            position: absolute;
            left: 50%;
            padding-left: 2px;
            top: 4px;
            bottom: 182px;
            right: 4px;
        }

        #spillmeny {
            position: absolute;
            left: 4px;
            bottom: 58px;
            height: 120px;
            right: 4px;
        }

        #oddsenticker {
            position: absolute;
            left: 4px;
            bottom: 4px;
            height: 50px;
            right: 4px;
        }
    </style>
    <script type="text/javascript" src="../Scripts/jquery-1.10.2.js"></script>
    <script type="text/javascript">
        var pid = "APDM";
        var id = [30,31];
        var color = "bebebe";

        var src = {
            ".nt-widget-30 iframe" : "../Widget30.aspx?id=#ID0#",
            ".nt-widget-31 iframe" : "../Widget30.aspx?id=#ID1#",
            "#spillmeny iframe" : "../iframes/Spillmeny/spillmeny.aspx?WT.mc_id=#PID#_forsiden_spillmeny&header=false",
            "#oddsenticker iframe" : "LangoddsenTicker.aspx?custom=forsiden&WT.mc_id=#PID#_forsiden_oddsen20ticker"
        };

        $(document).ready(function() {

            if(color != "") {
                if(color.match(/[0-9a-f]{3}|[0-9a-f]{6}/g))
                    $("body").css("background-color", "#"+color);
                else
                    $("body").css("background-color", color);
            }

            for(var i in src) {                
                $(i).prop("src", src[i].replace("#PID#", pid).replace("#ID0#", id[0]).replace("#ID1#", id[1]));
            }
        });
    </script>
</head>
<body>
    <form method="post" action="Frontbox.aspx" id="form1">
<div class="aspNetHidden">
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwULLTE0OTcxNDAxNTlkZE5LIqTfTuB7iwPFQgZibQ828rK6KyeciFVao7ezzbDk" />
</div>
</form>
    <!-- oddsen 3.0 venstre -->
    <div class='nt-widget nt-widget-30' ><iframe src='' frameborder='0' style='width:100%;height:100%;border:0'></iframe></div>

    <!-- oddsen 3.0 høyre -->
    <div class='nt-widget nt-widget-31' ><iframe src='' frameborder='0' style='width:100%;height:100%;border:0'></iframe></div>

    <!-- spillmeny -->
    <div id="spillmeny">
        <iframe frameborder='0' scrolling='no' allowTransparency='true' style='width:100%;height:100%;border:0' src=''></iframe>
    </div>
    <!-- oddsenticker -->
    <div id="oddsenticker">
        <iframe frameborder='0' scrolling='no' allowTransparency='true' style='width:100%;height:100%;border:0' src=''></iframe>
    </div>
</body>
</html>
