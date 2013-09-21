Type=Activity
Version=2.71
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim SelectedStageID As Long
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim scvStages As ScrollView 
	Dim btnStageAdd As Button
	Dim btnStageDelete As Button 
	Dim btnStageUp As Button 
	Dim btnStageDown As Button 
	Dim btnStageEdit As Button 
	
	Type SortItem(StageID As Long, SwapStageID As Long, CurrentSortOrder As Long, NewSortOrder As Long)
	
End Sub	

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("StageList")
'	scvStages.Initialize2(500, "scvProfiles")
'	Activity.AddView(scvStages, 0%x, 0%y, 100%x, 50%y)
	
	If FirstTime Then
'		btnStageAdd.Initialize("btnStageAdd")
'	  btnStageDelete.Initialize("btnStageDelete")
'	  btnStageUp.Initialize("btnStageUp")
'	  btnStageDown.Initialize("btnStageDown")
'		btnStageEdit.Initialize("btnStageEdit")
	End If

End Sub

Sub Activity_Resume
	SelectedStageID = -99
	LoadSQLTables 
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	'SelectedStageID = -99
End Sub


#Region SQL
Sub LoadSQLTables
	
	Dim sStageType As String 
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
	
	If ProfileList.SelectedProfileID <> -1 Then	'do we have a profile selected
	  Cursor1 = Main.SQL1.ExecQuery("SELECT stage_id, stage_type, stage_temp, stage_percent, stage_seconds, stage_sort_order FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID &  " ORDER BY stage_sort_order")
	  For i = 0 To Cursor1.RowCount - 1				
				Dim pnlAdd As Panel 
				Dim lbl As Label
				pnlAdd.Initialize("pnlAdd")
				scvStages.Panel.AddView(pnlAdd,0,5dip+i*50dip,100%x,50dip)
				
	      Cursor1.Position = i         	      
				Dim lbl As Label
				lbl.Initialize("lbl")
				sStageType = StageTypeToString(Cursor1.Getstring("stage_type"))
				lbl.Text = 	Cursor1.Getstring("stage_sort_order") & " : "  & sStageType & " : " & _
										Cursor1.Getstring("stage_temp") & " C : " & _
										Cursor1.Getstring("stage_percent") & " % : " & _
										Cursor1.Getstring("stage_seconds") & " sec"
				
				pnlAdd.AddView(lbl, 0%x, 5dip, 100%x, 40dip)      
	      lbl.TextSize=15
				lbl.TextColor = Colors.Black 
				pnlAdd.Tag = Cursor1.GetInt("stage_id")
				lbl.Tag = pnlAdd
								
	  Next
	  Cursor1.Close
	End If
	scvStages.Panel.Height=50%y
		
End Sub

Sub ClearList
	'Clears the table  
	Dim i As Int 
	For i = scvStages.Panel.NumberOfViews -1 To 0 Step -1
		scvStages.Panel.RemoveViewAt(i)
	Next 
	
End Sub

Sub GetStageIDFromOrder(tSortItem As SortItem) As Long 
	'If we have sort_order, then find the stageID using currentSortORder
	Dim ret As Long 
	If tSortItem.CurrentSortOrder > -1 Then
		Dim sSQL As String
		sSQL = "SELECT stage_id FROM stages WHERE sort_order=" & tSortItem.CurrentSortOrder  		
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		tSortItem.StageID = ret
		Return ret
	Else
		Msgbox("Error getting current sort order", "1:Error")
		Return -1
	End If
End Sub

Sub GetCurrentOrder(tSortItem As SortItem) As Long 
	'Finds the sort_order a record, returns also ByRef
	'If we have StageID, then find the sort_order
	Dim ret As Object 
	If tSortItem.StageID > -1 Then
		Dim sSQL As String
		sSQL = "SELECT stage_sort_order FROM stages WHERE stage_id=" & tSortItem.StageID 		
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		
		If ret <> Null Then
			tSortItem.CurrentSortOrder = ret
		End If
		
		Return ret
	Else
		Msgbox("Error getting current sort order", "2:Error")
		Return -1
	End If
End Sub

Sub GetHigherSortOrder(tSortItem As SortItem) As Long
	Dim ret As Long 
	If tSortItem.CurrentSortOrder > -1 Then
		Dim sSQL As String
		sSQL = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID & " AND stage_sort_order > " & tSortItem.CurrentSortOrder
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		tSortItem.NewSortOrder = ret
		
		'We now need to get the stage ID with this order
		sSQL = "SELECT stage_id FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID & " AND stage_sort_order = " & tSortItem.NewSortOrder 
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		tSortItem.SwapStageID = ret
		
		Return ret

	Else
		Msgbox("Error getting current sort order", "Error")
		Return -1
	End If
End Sub


Sub GetLowerSortOrder(tSortItem As SortItem) As Long
	Dim ret As Long 
	If tSortItem.CurrentSortOrder > -1 Then
		Dim sSQL As String
		sSQL = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID & " AND stage_sort_order < " & tSortItem.CurrentSortOrder
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		tSortItem.NewSortOrder = ret
		
		'We now need to get the stage ID with this order
		sSQL = "SELECT stage_id FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID & " AND stage_sort_order = " & tSortItem.NewSortOrder 
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		tSortItem.SwapStageID = ret
		
		Return ret

	Else
		Msgbox("Error getting current sort order", "Error")
		Return -1
	End If
End Sub

Sub SwapSortOrder(tSortItem As SortItem)
'	Dim ret As Long 
	If tSortItem.StageID  > -1 AND tSortItem.SwapStageID > -1 Then
		Main.SQL1.BeginTransaction 
		
		Dim sSQL As String
		sSQL = "UPDATE stages SET stage_sort_order = " & tSortItem.NewSortOrder & " WHERE stage_id = " & tSortItem.StageID 
		Main.SQL1.ExecNonQuery(sSQL)
		
		'We now need to get the stage ID with this order
		sSQL = "UPDATE stages SET stage_sort_order = " & tSortItem.CurrentSortOrder & " WHERE stage_id = " & tSortItem.SwapStageID 
		Main.SQL1.ExecNonQuery(sSQL)
		
		Main.SQL1.TransactionSuccessful
		Main.SQL1.EndTransaction
		
	Else
		Msgbox("Error getting current sort orders and stages", "Error")
		Return -1
	End If
End Sub

Sub GetMaxOrder() As Long
	Dim ret As Long 
	If ProfileList.SelectedProfileID > -1 Then
		Dim sSQL As String
		sSQL = "SELECT MAX(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID 
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		
'		If ret = Null Then
			Return ret
'		Else
'			Return 
'		End If	
	Else
		Msgbox("Error getting sort order", "Error")
		Return -1
	End If
End Sub

Sub GetMinOrder() As Long
	Dim ret As Long 
	If ProfileList.SelectedProfileID > -1 Then
		Dim sSQL As String
		sSQL = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID 
		ret = Main.SQL1.ExecQuerySingleResult(sSQL)
		
'		If ret = Null Then
			Return ret
'		Else
'			Return 
'		End If	
	Else
		Msgbox("Error getting sort order", "Error")
		Return -1
	End If
End Sub

Sub SetOrder(lOrderID As Long, lStageID As Long)
	'Dim ret As Long 
	If lStageID = -1 Then
		Dim sSQL As String
		sSQL = "SELECT MIN(stage_sort_order) FROM stages WHERE profile_id=" & ProfileList.SelectedProfileID 
		
	Else
		Msgbox("Error getting sort order", "Error")
		Return -1
	End If
End Sub


#Region StageType
Sub StageTypeToString(iStageType As Int) As String 
	Select iStageType
		Case 1
			Return "Do nothing"
		Case 2
			Return "Heat to"
		Case 3
			Return "Beep"
		
	End Select
End Sub

Sub StringToStageType(sStageType As String) As Int 
	Select sStageType
		Case "Do nothing"
			Return 1
		Case "Heat to"
			Return 2
		Case "Beep"
			Return 3
		
	End Select
End Sub
#End Region 

#End Region

Sub btnStageDelete_Click()
	
	Dim ret As Int 
	
	If SelectedStageID <> -99 Then
		ret = Msgbox2("Delete selected stage?", "Delete", "Delete", "Cancel","", Null)
		If ret = DialogResponse.POSITIVE Then			
			Main.SQL1.ExecNonQuery("DELETE FROM stages WHERE stage_id =" & SelectedStageID)			
			LoadSQLTables
		End If
	Else
		Msgbox("Select a stage to delete", "No selection") 
	End If
End Sub

Sub SelectPanel(pnl As Panel)
	Dim v As View 	
	Dim i As Int 
	
	For i = scvStages.Panel.NumberOfViews -1 To 0 Step -1
		v = scvStages.Panel.GetView (i)
		If v Is Panel Then
			v.Color = Colors.white   
		End If
	Next 
	
	
	'Mark the selected row
	pnl.Color = Colors.LightGray 
	
	'Remember the id of the selected item
	SelectedStageID  = pnl.Tag 	
End Sub

Sub pnlAdd_Click
	'Change the selected profile
	Dim send As Panel
	send = Sender
	SelectPanel(send)
	
End Sub

Sub lbl_Click	
	Dim Send As Label
	Dim pnl As Panel 
  Send=Sender
	pnl = Send.tag
  SelectPanel(pnl)
End Sub

Sub btnStageAdd_Click
	'Make a dummy id
	SelectedStageID = -98
	StartActivity(EditStage)
End Sub

Sub btnStageEdit_Click
	If SelectedStageID < -95 Then
		Msgbox("Please select a stage to edit", "Select")
	Else
		StartActivity(EditStage)
	End If
End Sub

Sub btnStageDown_Click

	'Initialise the sort tiem. Note "types" are passed byref
	Dim tCurrentSort As SortItem 'Item selected
	
	tCurrentSort.StageID = SelectedStageID 		'
	tCurrentSort.SwapStageID = -1							' the stage that we're swapping into
	tCurrentSort.CurrentSortOrder = -1				' sort order of the selected stage
	tCurrentSort.NewSortOrder = 1							' our new sort order
	
	If SelectedStageID > -95 Then
		'Get the current item sort order
		If GetCurrentOrder(tCurrentSort) > 0 Then
			
			If GetMaxOrder > tCurrentSort.CurrentSortOrder  Then
				'Get the next higher sort_order and stage_id and save it in this type
				GetHigherSortOrder(tCurrentSort)
				
				'Do the actual swap
				SwapSortOrder(tCurrentSort)				
				
				'Reload the list
				LoadSQLTables 
			Else
				Msgbox("Already at the end", "End")
			End If			
		Else
			Msgbox("No sort order found", "Error")
		End If
	Else
		Msgbox("Select a stage", "No Selection")
	End If
End Sub

Sub btnStageUp_Click
	'Initialise the sort tiem. Note "types" are passed byref
	Dim tCurrentSort As SortItem 'Item selected
	
	tCurrentSort.StageID = SelectedStageID 		'
	tCurrentSort.SwapStageID = -1							' the stage that we're swapping into
	tCurrentSort.CurrentSortOrder = -1				' sort order of the selected stage
	tCurrentSort.NewSortOrder = 1							' our new sort order
	
	If SelectedStageID > -95 Then
		'Get the current item sort order
		If GetCurrentOrder(tCurrentSort) > 0 Then
			
			If GetMinOrder < tCurrentSort.CurrentSortOrder  Then
				'Get the next lower sort_order and stage_id and save it in this type
				GetLowerSortOrder(tCurrentSort)
				
				'Do the actual swap
				SwapSortOrder(tCurrentSort)				
				
				'Reload the list
				LoadSQLTables 
			Else
				Msgbox("Already at the start", "End")
			End If			
		Else
			Msgbox("No sort order found", "Error")
		End If
	Else
		Msgbox("Select a stage", "No Selection")
	End If
End Sub