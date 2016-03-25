<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <link rel="icon" href="favicon.ico"/>

    <title>Cloud Native Login</title>

    <!-- Bootstrap core CSS -->
    <link href="resources/dist/css/bootstrap.min.css" rel="stylesheet"/>

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="resources/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="resources/css/signin.css" rel="stylesheet"/>

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]>
    <script src="resources/assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="resources/assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <h2>Please Confirm</h2>

    <p>
        Do you authorize "${authorizationRequest.clientId}" at "${authorizationRequest.redirectUri}" to access your
        protected resources
        with scope ${authorizationRequest.scope?join(", ")}.
    </p>
    <form id="confirmationForm" name="confirmationForm"
          action="oauth/authorize" method="post">
        <input name="user_oauth_approval" value="true" type="hidden"/>
        <input name="scope.openid" value="true" type="hidden"/>
        <#--<input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
        <button class="btn btn-primary" type="submit">Approve</button>
    </form>
    <form id="denyForm" name="confirmationForm"
          action="oauth/authorize" method="post">
        <input name="user_oauth_approval" value="false" type="hidden"/>
        <#--<input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
        <button class="btn btn-primary" type="submit">Deny</button>
    </form>
</div>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
<script src="https://code.jquery.com/jquery-2.2.2.min.js"
        integrity="sha256-36cp2Co+/62rEAAYHLmRCPIych47CvdM+uTBJwSzWjI=" crossorigin="anonymous"></script>
<script src="resources/js/main.js"></script>
</body>
</html>