Type=Activity
Version=2.71
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

'******************************************************
'Communication between Android and Board
'All messages terminate with CRLF
'Send Profile to Board Format:
'		;		delimiter between everything
'		[		start of stage
'
'
	
	
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim SelectedProfileID As Long : SelectedProfileID = -1
	Dim SelectedProfileName As String 
	Dim AStream As AsyncStreams			'USed for Bluetooth
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim scvProfiles As ScrollView
	Dim btnNewProfile As Button 
	Dim btnRename As Button 
	Dim btnDeleteProfile As Button 
	Dim txtLog As EditText  
	Dim lblTemperature As Label
	
	Dim sMsgBuild As String 
	
	Dim sf As StringFunctions
  
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("ProfileList")
'	scvProfiles.Initialize2(500, "scvProfiles")
'	Activity.AddView(scvProfiles, 0%x, 0%y, 100%x, 50%y)
	
	'Bluetooth streams
	If AStream.IsInitialized = False AND Main.SkipBT = False Then	
		AStream.Initialize(Main.serial1.InputStream, Main.serial1.OutputStream, "AStream")
	End If

	
	If FirstTime Then		
		sf.Initialize
	End If
	
	LoadProfiles(-1)
End Sub

Sub Activity_Resume
	SelectedProfileID = -1
	LoadProfiles (-1)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	'SelectedProfileID = -1
End Sub

#Region Bluetooth
Sub AStream_NewData (Buffer() As Byte)
	sMsgBuild = sMsgBuild & BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	If sMsgBuild.EndsWith(CRLF) Then	
		ProcessMessages(sMsgBuild)
		sMsgBuild = ""
	End If	
End Sub

Sub AStream_Error
	ToastMessageShow("Connection is broken.", True)
End Sub

Sub AStream_Terminated
	AStream_Error
End Sub

Sub ProcessMessages (Msg As String)
	'Split out the CRLF's
	'Dim sSendString As String 
	Dim MsgList As List 
	Dim i As Int 
	
	'If there is just one CRLF then send it to be processed.
	If Msg.IndexOf(CRLF) = Msg.Length - 1 Then
		ProcessMessage(Msg)
	Else	'We need to split the strings
		MsgList = sf.Split(Msg, CRLF)
		
		For i = 0 To MsgList.Size - 1
			ProcessMessage(MsgList.Get(i))			
		Next
		 
	End If
End Sub

Sub ProcessMessage (Msg As String)
	'Take the string message from Arduino and strip it into pieces.
	
	'	C: Temperature
	' P: Profile Name
	' S: Stage number that's starting
	' D: Profile complete
	' B: Beep
	
'	If Msg.IndexOf(CRLF) <> Msg.Length - 1 Then
'		Log("FOUND AT: " & Msg.IndexOf(CRLF) & "  - LEN:" & (Msg.Length - 1))
'	End If
	
	If Msg <> "" AND Msg.Length > 1 Then
	
		Dim sType As String 
		Dim sWorking As String 'Dummy to build strings
		
		'txtLog.Text = Msg & txtLog.Text' & CRLF
		
		'If sType.Length >2 Then
			sType = Msg.SubString2(0,2)
			
			Select sType
				Case "C:"	'Temperature reading
					sWorking = Msg.Replace("C:","")
					sWorking = sWorking.Replace(CRLF, "")
					lblTemperature.Text = sWorking & " " & Chr(0x00B0) & "C" 
					sWorking = ""
			
				Case "B:" 'Beep
					Dim b As Beeper
					b.Initialize(600, 500) '600 milliseconds, 500 hz
					b.Beep
					txtLog.Text = "BEEP !!" & CRLF & txtLog.Text
					
				Case "P:"	'Profile is starting
					txtLog.Text = "Profile: " & Msg.SubString(2) & CRLF & txtLog.Text
				
				Case "S:" 'Stage is starting
					txtLog.Text = "Stage: " & Msg.SubString(2) & CRLF & txtLog.Text
					
				Case "D:" 'PRofile complete
					txtLog.Text = "Complete!" & CRLF & txtLog.Text
					
				Case "E:" 'Profile saved to EEPROM
					txtLog.Text = "Uploaded:" & Msg.SubString(2) & CRLF & txtLog.Text
					
				Case Else
					Log(Msg)
					txtLog.Text = Msg & CRLF & txtLog.Text 
					
			End Select
		'End If
		
		'Only keep the first 2000 characters in the text log.
		If txtLog.Text.Length > 2000 Then
			txtLog.Text = txtLog.text.SubString2(0,2000)
		End If
	End If
	
	
End Sub


Sub SendProfile
'		P:	xxxxxxxxxxxxxxx	name of profile (max 50 characters)
'		I:	xx  stage ID 00 to 99
'		Each part of the profile is padded out to the the correct number of characters
'

	Dim ret As String	
	Dim sProfile As String 
	Dim i As Int 	
	
	If SelectedProfileID > -1 Then
		'ret = Msgbox2("Are you sure you want to upload a new profile?", "Upload", "Yes", "", "No", Null)
		
		Dim iCount As Int 
		Dim sCount As String 
		
		iCount = Main.SQL1.ExecQuerySingleResult("SELECT count(*) FROM stages WHERE profile_id = " & SelectedProfileID)
		sCount = iCount
		
		'If ret = DialogResponse.POSITIVE Then
		sProfile = SelectedProfileName
		If sProfile.Length > 20 Then sProfile = sProfile.SubString2(0, 19) 'max 20 characters
		sProfile = sProfile & "[" & PadStringTo2(sCount)							     'Prepare the start of the profile			
		
		Dim Cursor1 As Cursor
	  Cursor1 = Main.SQL1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id=" _
																	& SelectedProfileID & " ORDER BY stage_sort_order")
	  For i = 0 To Cursor1.RowCount - 1							
    	Cursor1.Position = i      
			sProfile = sProfile & "["
			sProfile = sProfile & Cursor1.GetString("stage_type") _
						 & PadStringTo3(Cursor1.GetString("stage_temp")) _
						 & PadStringTo3(Cursor1.GetString("stage_percent")) _ 
						 & PadStringTo3(Cursor1.GetString("stage_seconds"))
		Next
		Cursor1.Close									
			
		'End If
	
		'sProfile = "Test Message"
		Log(sProfile)
		'Log(sProfile.Length)
		
		SendData(sProfile & CRLF)
	Else
		Msgbox("Select a profile to upload", "No Profile")
	End If
End Sub

Sub PadStringTo2(inString As String) As String 
	Dim iValue As Int
	Dim retStr As String 
	iValue = inString	
	
	If iValue < 10 Then
		retStr = "0" & inString
	Else
		retStr = inString
	End If 
	
	Return retStr
End Sub

Sub PadStringTo3(inString As String) As String 
	Dim iValue As Int
	Dim retStr As String 
	iValue = inString	
	
	If iValue < 10 Then
		retStr = "00" & inString
	Else If iValue < 100 Then
		retStr = "0" & inString
	Else
		retStr = inString
	End If 
	
	Return retStr
End Sub

Sub SendData(SendString As String)
	AStream.Write(SendString.GetBytes("UTF8"))	
End Sub


#End Region

#Region SQL
Sub LoadProfiles(lSelectID As Long)
	'Dim sCheckProfiles As String 
	'SQL3.ExecNonQuery("DROP TABLE IF EXISTS profiles") 
	Main.SQL1.ExecNonQuery("CREATE TABLE IF NOT EXISTS profiles (profile_id INTEGER PRIMARY KEY AUTOINCREMENT, profile_name TEXT)")
	
	'Main.SQL1.ExecNonQuery("DROP TABLE IF EXISTS stages") 
	Main.SQL1.ExecNonQuery("CREATE TABLE IF NOT EXISTS stages  	(stage_id INTEGER PRIMARY KEY AUTOINCREMENT, " & _
																															"profile_id INTEGER, " & _
																															"stage_type INTEGER,  " & _
																															"stage_temp INTEGER,  " & _
																															"stage_percent INTEGER,  " & _
																															"stage_seconds INTEGER,  " & _
																															"stage_sort_order INTEGER)")
	
	'Get rid of any current entries
	ClearList
	
	Dim Cursor1 As Cursor
  Cursor1 = Main.SQL1.ExecQuery("SELECT profile_id, profile_name FROM profiles")
  For i = 0 To Cursor1.RowCount - 1				
			Dim pnlAdd As Panel 
			Dim lbl As Label
			pnlAdd.Initialize("pnlAdd")
			scvProfiles.Panel.AddView(pnlAdd,0,5dip+i*50dip,100%x,50dip)
			
      Cursor1.Position = i      
      'Log(Cursor1.GetInt("profile_id"))
      'Log(Cursor1.GetString("profile_name"))
			Dim lbl As Label
			lbl.Initialize("lbl")
			lbl.Text = Cursor1.Getstring("profile_name")
			'lbl.Tag = Cursor1.GetInt("profile_id")
			pnlAdd.Color = Colors.White 
			pnlAdd.AddView(lbl,10dip, 5dip, 80dip, 40dip)      
      lbl.TextSize=20
			lbl.TextColor = Colors.DarkGray 
			If Cursor1.GetInt("profile_id") = lSelectID Then
				lbl.TextColor = Colors.Black  
				pnlAdd.Color = Colors.LightGray 
				SelectedProfileID  = lSelectID
				SelectedProfileName = Cursor1.Getstring("profile_name")
			End If
			pnlAdd.Tag = Cursor1.GetInt("profile_id")
			lbl.Tag = pnlAdd
								
  Next
  Cursor1.Close
	
	scvProfiles.Panel.Height = 50dip * i
	DoEvents

		
	scvProfiles.Panel.Height=50%y
		
End Sub

Sub ClearList
	'Clears the table  
	Dim i As Int 
	For i = scvProfiles.Panel.NumberOfViews -1 To 0 Step -1
		scvProfiles.Panel.RemoveViewAt(i)
	Next 
			
	SelectedProfileID = -1	'Nothing selected
	SelectedProfileName = "" 'No name
	
End Sub

#End Region

#Region Events not buttons
Sub pnlAdd_Click
	'Change the selected profile
	Dim Send As Panel
	Send = Sender
'	SelectPanel(send)
	LoadProfiles(Send.Tag)
	
End Sub

Sub lbl_Click	
	Dim Send As Label
	Dim pnl As Panel 
  Send=Sender
	pnl = Send.tag
	LoadProfiles(pnl.Tag)
  'SelectPanel(pnl)
End Sub

#End Region 

#Region Buttons
Sub btnNewProfile_Click
	Dim Id As InputDialog
	Dim ret As String 
	Id.InputType = Id.INPUT_TYPE_TEXT 
	Id.Input = ""
	Id.Hint = "Enter the name of the profile"
	Id.HintColor = Colors.ARGB(196, 255, 140, 0)
	ret = DialogResponse.CANCEL
	ret = Id.Show("Input the name of the profile", "New Profile", "OK", "Cancel", "", Null)
	If Id.Response <> DialogResponse.CANCEL Then
		Main.SQL1.ExecNonQuery("INSERT INTO profiles VALUES (Null,'" & Id.Input & "')")
		LoadProfiles(-1)
	End If
End Sub

Sub btnEditStages_Click()
	If SelectedProfileID <> -1 Then
		StartActivity(StageList)
	Else
		Msgbox("Select a profile to edit stages", "No selection")
	End If
End Sub

Sub btnRename_Click
	Dim Id As InputDialog
	Dim ret As String
	
	If SelectedProfileID <> -1 Then	
		
		Id.InputType = Id.INPUT_TYPE_TEXT 
		Id.Input = ""
		Id.Hint = "Enter the NEW name of the profile"
		Id.HintColor = Colors.ARGB(196, 255, 140, 0)
		ret = DialogResponse.CANCEL
		ret = Id.Show("Input the name of the profile", "Rename Profile", "OK", "Cancel", "", Null)
		If Id.Response <> DialogResponse.CANCEL Then
			Main.SQL1.ExecNonQuery("UPDATE profiles SET profile_name ='" & Id.Input.SubString2(0, 48)  & "' WHERE profile_id = " & SelectedProfileID)
			LoadProfiles(-1)
		End If
	Else
		Msgbox("Select a profile to rename", "No selection")
	End If
End Sub

Sub btnDeleteProfile_Click
	
	Dim ret As Int 
	
	If SelectedProfileID <> -1 Then
		ret = Msgbox2("Delete selected profile AND stages?", "Delete", "Delete", "Cancel","", Null)
		If ret = DialogResponse.POSITIVE Then			
			Main.SQL1.ExecNonQuery("DELETE FROM stages WHERE profile_id =" & SelectedProfileID)
			Main.SQL1.ExecNonQuery("DELETE FROM profiles WHERE profile_id =" & SelectedProfileID)			
			LoadProfiles(-1)
		End If
	Else
		Msgbox("Select a profile to DELETE", "No selection")
	End If
End Sub

Sub btnUpload_Click
	SendProfile		
End Sub

Sub btnGO_Click
	SendData("GO" & CRLF)
End Sub

#End Region 



