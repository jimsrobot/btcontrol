Type=Service
Version=2.71
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim admin As BluetoothAdmin
	Dim serial1 As Serial
	Dim SQL1 As SQL 
	Dim AStream As AsyncStreams			'USed for Bluetooth
	Dim SkipBT As Boolean 
	Dim sMsgBuild As String 
	Dim serialList As List 
	Dim newestMsg As String 
	Dim oldestMsg As String 
	Dim SendString As String 
End Sub

Sub Service_Create
	admin.Initialize("admin")
	serial1.Initialize("serial1")
	SQL1.Initialize(File.DirDefaultExternal, "wirelessreflow.db", True)
End Sub

Sub Service_Start (StartingIntent As Intent)

End Sub

Sub Service_Destroy

End Sub

Sub StartAstreams
	'Bluetooth streams
	If AStream.IsInitialized = False AND SkipBT = False Then	
		AStream.Initialize(serial1.InputStream, serial1.OutputStream, "AStream")
	End If
End Sub

Sub AStream_NewData (Buffer() As Byte)
	sMsgBuild = sMsgBuild & BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	If sMsgBuild.EndsWith(CRLF) Then	
		AddMessage(sMsgBuild)
		sMsgBuild = ""
		
		CallSub(ProfileList, "ProcessMessage")
	End If	
End Sub

Sub AStream_Error
	ToastMessageShow("Connection is broken.", True)
End Sub

Sub AStream_Terminated
	AStream_Error
End Sub

Sub SendData
	AStream.Write(SendString.GetBytes("UTF8"))	
End Sub

Sub AddMessage(inMsg As String)
	newestMsg = inMsg
	serialList.Add(inMsg)
End Sub

Sub GetOldestMsg
	If serialList.Size > 0 Then
		oldestMsg = serialList.Get(0)
		serialList.RemoveAt(0)
	Else
		oldestMsg = ""
	End If	
End Sub
