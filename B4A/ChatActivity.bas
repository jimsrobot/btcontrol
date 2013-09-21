Type=Activity
Version=2.71
@EndOfDesignText@
#Region Module Attributes
	#FullScreen: False
	#IncludeTitle: True
#End Region

'Activity module
Sub Process_Globals
	Dim AStream As AsyncStreams
End Sub

Sub Globals
	Dim txtLog As EditText
	Dim lblTemp As Label 
	Dim lblCurrentProfile As Label 
	Dim btnProfiles As Button 
	Dim btnQuit As Button 
	
	Dim sMsgBuild As String 
	'Dim SQL2 As SQL
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("main")
	
	If AStream.IsInitialized = False AND Main.SkipBT = False Then
		'AStream.InitializePrefix(Main.serial1.InputStream, True, Main.serial1.OutputStream, "AStream")
		AStream.Initialize(Main.serial1.InputStream, Main.serial1.OutputStream, "AStream")
	End If
	
	If FirstTime Then
		'SQL2.Initialize(File.DirDefaultExternal, "wirelessreflow.db", True)
		'LoadSQLTables
	End If
	'txtLog.Initialize("click")
End Sub

Sub AStream_NewData (Buffer() As Byte)
	sMsgBuild = sMsgBuild & BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	If sMsgBuild.EndsWith(CRLF) Then
		LogMessage(sMsgBuild)
		sMsgBuild = ""
	End If
	'LogMessage(BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
End Sub

Sub AStream_Error
	ToastMessageShow("Connection is broken.", True)
End Sub

Sub AStream_Terminated
	AStream_Error
End Sub

Sub Activity_Resume
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		AStream.Close
	End If
End Sub

Sub LogMessage(Msg As String)
	txtLog.Text = txtLog.Text & Msg' & CRLF
	txtLog.SelectionStart = txtLog.Text.Length
	lblTemp.Text = Msg.SubString2(0, Msg.Length -1) & " C"
End Sub

Sub btnProfiles_Click
	StartActivity("ProfileList")
End Sub

Sub btnQuit_Click
	ExitApplication
	Activity.Finish 
End Sub